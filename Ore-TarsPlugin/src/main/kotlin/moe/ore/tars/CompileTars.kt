package moe.ore.tars

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

object CompileTars {

    fun compileDir(input : ByteArray) : ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val cr = ClassReader(input)

        val cv = object : ClassVisitor(ASM9, cw) {
            private lateinit var className : String
            private var isTarsClass = false

            private var requireRead : Boolean = false
            private var requireWrite : Boolean = false
            private var servantName : String = ""
            private var funcName : String = ""
            private var reqName : String = ""
            private var respName : String = ""

            private lateinit var fieldMap : HashMap<Int, FieldInfo>

            override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
                this.className = name
                super.visit(version, access, name, signature, superName, interfaces)
            }

            override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
                if(descriptor == "Lmoe/ore/tars/TarsClass;") {
                    this.isTarsClass = true
                    this.fieldMap = hashMapOf()
                }
                return object : AnnotationVisitor(ASM9, super.visitAnnotation(descriptor, visible)) {
                    override fun visit(name: String?, value: Any?) {
                        if(isTarsClass && name != null) {
                            when(name) {
                                "requireRead" -> requireRead = value as Boolean
                                "requireWrite" -> requireWrite = value as Boolean
                                "servantName" -> servantName = value as String
                                "funcName" -> funcName = value as String
                                "reqName" -> reqName = value as String
                                "respName" -> respName = value as String
                            }
                            // 如果结束方法可以消去注解
                            return
                        }
                        super.visit(name, value)
                    }
                }
            }

            override fun visitField(
                access: Int,
                name: String,
                fieldType: String,
                signature: String?,
                value: Any?
            ): FieldVisitor {
                return object : FieldVisitor(ASM9, super.visitField(access, name, fieldType, signature, value)) {
                    // 读取注解获取tag
                    override fun visitAnnotation(annotationType: String?, visible: Boolean): AnnotationVisitor {
                        val isTarsField = annotationType == "Lmoe/ore/tars/TarsField;"
                        return object : AnnotationVisitor(ASM9, super.visitAnnotation(annotationType, visible)) {
                            private var tag : Int = 0
                            private var require : Boolean = false

                            override fun visit(name: String?, value: Any?) {
                                if (isTarsClass && isTarsField) {
                                    when(name) {
                                        "id" -> tag = value as Int
                                        "require" -> require = value as Boolean
                                    }
                                    return
                                }
                                super.visit(name, value)
                            }

                            override fun visitEnd() {
                                if(isTarsField) {
                                    fieldMap[tag] = FieldInfo(
                                        name = name,
                                        id = tag,
                                        type = fieldType,
                                        require = require
                                    )
                                }
                                super.visitEnd()
                            }
                        }
                    }
                }
            }

            override fun visitEnd() {
                if(isTarsClass) {
                    // 合成参数方法
                    servantName(servantName)
                    funcName(funcName)
                    reqName(reqName)
                    respName(respName)

                    if(requireRead) readFrom(className, fieldMap)
                    if(requireWrite) writeTo(className, fieldMap)
                }
                super.visitEnd()
            }
        }

        cr.accept(cv, 0) // 将cr代理给cv

        return cw.toByteArray()
    }

    private fun ClassVisitor.writeTo(className: String, fieldMap: Map<Int, FieldInfo>) {
        visitMethod(ACC_PUBLIC, "writeTo", "(Lmoe/ore/tars/TarsOutputStream;)V", null, null).also { mv ->
            mv.visitCode()
            var nowLabel = Label()
            var nextLabel = Label()
            mv.visitLabel(nowLabel)
            fieldMap.forEach { (tag, field) ->
                mv.visitFrame(F_SAME, 0, null, 0, null)
                if(field.needCheckNull()) {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitFieldInsn(GETFIELD, className, field.name, field.type)
                    mv.visitJumpInsn(IFNULL, nextLabel)
                }

                mv.visitVarInsn(ALOAD, 1)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitFieldInsn(GETFIELD, className, field.name, field.type)

                mv.visitIntInsn(SIPUSH, tag)

                if(field.isBaseType()) {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "moe/ore/tars/TarsOutputStream", "write", "(${field.type}I)V", false)
                } else {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "moe/ore/tars/TarsOutputStream", "write", "(Ljava/lang/Object;I)V", false)
                }

                nowLabel = nextLabel
                mv.visitLabel(nowLabel)
                nextLabel = Label()
            }
            mv.visitFrame(F_SAME, 0, null, 0, null)
            mv.visitInsn(RETURN)
            mv.visitMaxs(3, 2)
            mv.visitEnd()
        }
    }

    private fun ClassVisitor.readFrom(className : String, fieldMap : Map<Int, FieldInfo>) {
        visitMethod(ACC_PUBLIC, "readFrom", "(Lmoe/ore/tars/TarsInputStream;)V", null, null).also { mv ->
            mv.visitCode()
            fieldMap.forEach { (tag, field) ->
                println("compile field --> $field")

                mv.visitVarInsn(ALOAD, 0)
                mv.visitVarInsn(ALOAD, 1)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitFieldInsn(GETFIELD, className, field.name, field.type)
                mv.visitIntInsn(SIPUSH, tag)
                mv.visitInsn(if(field.require) ICONST_1 else ICONST_0)

                if(field.isBaseType()) {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "moe/ore/tars/TarsInputStream", "read", "(${field.type}IZ)${field.type}", false)
                } else {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "moe/ore/tars/TarsInputStream", "read", "(Ljava/lang/Object;IZ)Ljava/lang/Object;", false)
                    mv.visitTypeInsn(CHECKCAST, field.type.substring(1).let {
                        it.substring(0, it.length - 1)
                    })
                }

                // mv.visitInsn(POP)
                mv.visitFieldInsn(PUTFIELD, className, field.name, field.type)

            }
            mv.visitInsn(RETURN)
            mv.visitMaxs(5, 2)
            mv.visitEnd()
        }
    }

    private fun ClassVisitor.servantName(servantName : String) {
        if (servantName.isNotEmpty()) {
            visitMethod(ACC_PUBLIC, "servantName", "()Ljava/lang/String;", null, null).also { mv ->
                mv.visitCode()
                mv.visitLdcInsn(servantName)
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()
            }
        }
    }

    private fun ClassVisitor.funcName(funcName : String) {
        if (funcName.isNotEmpty()) {
            visitMethod(ACC_PUBLIC, "funcName", "()Ljava/lang/String;", null, null).also { mv ->
                mv.visitCode()
                mv.visitLdcInsn(funcName)
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()
            }
        }
    }

    private fun ClassVisitor.reqName(reqName : String) {
        if (reqName.isNotEmpty()) {
            visitMethod(ACC_PUBLIC, "reqName", "()Ljava/lang/String;", null, null).also { mv ->
                mv.visitCode()
                mv.visitLdcInsn(reqName)
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()
            }
        }
    }

    private fun ClassVisitor.respName(respName : String) {
        if (respName.isNotEmpty()) {
            visitMethod(ACC_PUBLIC, "respName", "()Ljava/lang/String;", null, null).also { mv ->
                mv.visitCode()
                mv.visitLdcInsn(respName)
                mv.visitInsn(ARETURN)
                mv.visitMaxs(1, 1)
                mv.visitEnd()
            }
        }
    }
}