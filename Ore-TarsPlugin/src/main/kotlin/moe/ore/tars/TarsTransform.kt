package moe.ore.tars

import moe.ore.tars.util.AsmUtil
import moe.ore.tars.util.AsmUtil.getAnnotation
import moe.ore.tars.util.AsmUtil.hasAnnotation
import moe.ore.tars.util.AsmUtil.hasMethod
import moe.ore.tars.util.FileUtil
import moe.ore.tars.util.TarsUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
import java.io.File
import java.util.*


class TarsTransform(
    private val clzFile: File
) {
    private val bytes : ByteArray by lazy { FileUtil.readFile(clzFile) }
    private val fields : TreeMap<Int, FieldInfo> by lazy { TreeMap() }
    private val tarsClassInfo = TarsClass()
    private lateinit var className : String

    fun transform() {
        println("start transform by qiuluo...")
        // 做好解析工作
        println("tars task result : " + doLast(doFirst()))
    }

    private fun doLast(clz : ClassNode?) = clz?.runCatching {

        if (tarsClassInfo.requireWrite && !hasMethod("writeTo")) this.methods.add(MethodNode(ACC_PUBLIC, "writeTo", "(L$CLASS_TARS_OUTPUT;)V", null, null).also { mv ->
            mv.visitCode()
            var nowLabel = Label()
            var nextLabel = Label()
            mv.visitLabel(nowLabel)

            this@TarsTransform.fields.forEach { (tag, field) ->
                val name = field.name
                mv.visitFrame(F_SAME, 0, null, 0, null)

                field.bindMethod.forEach {
                    if(it.access and ACC_STATIC != 0) {
                        mv.visitMethodInsn(INVOKESTATIC, className, it.name, it.desc, false)
                    } else {
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitMethodInsn(INVOKEVIRTUAL, className, it.name, it.desc, false)
                    }
                }

                if(field.needCheckNull()) {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitFieldInsn(GETFIELD, className, name, field.type)
                    mv.visitJumpInsn(IFNULL, nextLabel)
                }
                mv.visitVarInsn(ALOAD, 1)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitFieldInsn(GETFIELD, className, name, field.type)
                if (field.isEnum) {
                    mv.visitMethodInsn(INVOKEVIRTUAL, field.type.let { it.substring(1, it.length - 1) }, "name", "()Ljava/lang/String;", false)
                }
                mv.visitIntInsn(SIPUSH, tag)
                if(field.isBaseType()) {
                    mv.visitMethodInsn(INVOKEVIRTUAL, CLASS_TARS_OUTPUT, "write", "(${field.type}I)V", false)
                } else {
                    mv.visitMethodInsn(INVOKEVIRTUAL, CLASS_TARS_OUTPUT, "write", "(Ljava/lang/Object;I)V", false)
                }
                nowLabel = nextLabel
                mv.visitLabel(nowLabel)
                nextLabel = Label()
            }
            mv.visitFrame(F_SAME, 0, null, 0, null)
            mv.visitInsn(RETURN)
            mv.visitMaxs(3, 2)
            mv.visitEnd()
        })

        if (tarsClassInfo.requireRead && !hasMethod("readFrom")) this.methods.add(MethodNode(ACC_PUBLIC, "readFrom", "(L$CLASS_TARS_INPUT;)V", null, null).also { mv ->
            mv.visitCode()
            mv.visitCode()
            this@TarsTransform.fields.forEach { (id, field) ->
                val name = field.name
                val type = field.type
                if(field.isEnum) {
                    // enum 独特的读取机制
                    val nextLabel = Label()
                    mv.visitVarInsn(ALOAD, 1)
                    mv.visitIntInsn(SIPUSH, id)
                    mv.visitInsn(if(field.require) ICONST_1 else ICONST_0)
                    mv.visitMethodInsn(INVOKEVIRTUAL, CLASS_TARS_INPUT, "readString", "(IZ)Ljava/lang/String;", false)
                    mv.visitVarInsn(ASTORE, 2)
                    mv.visitVarInsn(ALOAD, 2)
                    mv.visitJumpInsn(IFNULL, nextLabel)
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitVarInsn(ALOAD, 2)
                    mv.visitMethodInsn(INVOKESTATIC, field.type.let { it.substring(1, it.length - 1) }, "valueOf", "(Ljava/lang/String;)${field.type}", false)
                    mv.visitFieldInsn(PUTFIELD, className, field.name, field.type)
                    mv.visitLabel(nextLabel)
                    mv.visitFrame(F_APPEND, 1, arrayOf("java/lang/String"), 0, null)
                } else {
                    if(field.isTarsObject()) {
                        val cacheName = "cache_$name"
                        clz.fields.add(FieldNode(ACC_PUBLIC + ACC_STATIC, cacheName, type, null, null))
                        val gtLabel = Label()
                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                        mv.visitJumpInsn(IFNONNULL, gtLabel)
                        when {
                            type == "Ljava/util/HashMap;" -> {
                                // Ljava/util/HashMap<Ljava/lang/Byte;LTarsTest$Objsua;>;
                                val allType = field.sign!!.substring(19).let { it.substring(0, it.length - 2) }.split(";")
                                // 0 key 1 value 2 empty
                                val keyType = allType[0].substring(1)
                                val newClassName = allType[1].substring(1)
                                mv.visitTypeInsn(NEW, "java/util/HashMap")
                                mv.visitInsn(DUP)
                                mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
                                mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                when(keyType) {
                                    "java/lang/Byte" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Short" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Integer" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Long" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Float" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Double" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Character" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitIntInsn(BIPUSH, 48)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/String" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitLdcInsn("")
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    else -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)

                                        mv.visitTypeInsn(NEW, keyType)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, keyType, "<init>", "()V", false)

                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                }
                            }
                            type == "Ljava/util/Map;" -> {
                                // Ljava/util/Map<Ljava/lang/Byte;LTarsTest$Objsua;>;
                                val allType = field.sign!!.substring(15).let { it.substring(0, it.length - 2) }.split(";")
                                // 0 key 1 value 2 empty
                                val keyType = allType[0].substring(1)
                                val newClassName = allType[1].substring(1)
                                mv.visitTypeInsn(NEW, "java/util/HashMap")
                                mv.visitInsn(DUP)
                                mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
                                mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                when(keyType) {
                                    "java/lang/Byte" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Short" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Integer" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Long" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Float" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Double" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitInsn(ICONST_0)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/Character" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitIntInsn(BIPUSH, 48)
                                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false)
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    "java/lang/String" -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                        mv.visitLdcInsn("")
                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                    else -> {
                                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)

                                        mv.visitTypeInsn(NEW, keyType)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, keyType, "<init>", "()V", false)

                                        mv.visitTypeInsn(NEW, newClassName)
                                        mv.visitInsn(DUP)
                                        mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
                                        mv.visitInsn(POP)
                                    }
                                }
                            }
                            type == "Ljava/util/ArrayList;" -> {
                                try {
                                    // Ljava/util/ArrayList<Lxxx;>;
                                    val valueType = field.sign!!.substring(21).let { it.substring(0, it.length - 2) }
                                    // Lxxx;
                                    val newClassName = valueType.substring(1).let { it.substring(0, it.length - 1) }
                                    // list装的一定是对象 则不可能是 baseValue
                                    mv.visitTypeInsn(NEW, "java/util/ArrayList")
                                    mv.visitInsn(DUP)
                                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
                                    mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                    mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                    mv.visitTypeInsn(NEW, newClassName)
                                    mv.visitInsn(DUP)
                                    mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", true)
                                    mv.visitInsn(POP)
                                } catch (e : Exception) {
                                    e.printStackTrace()
                                }
                            }
                            type == "Ljava/util/List;" -> {
                                // list value 签名丢失将导致无法合成 list的cache 故请使用jdk6以上
                                try {
                                    // Ljava/util/List<Lxxx;>;
                                    val valueType = field.sign!!.substring(16).let { it.substring(0, it.length - 2) }
                                    // Lxxx;
                                    val newClassName = valueType.substring(1).let { it.substring(0, it.length - 1) }
                                    // list装的一定是对象 则不可能是 baseValue
                                    mv.visitTypeInsn(NEW, "java/util/ArrayList")
                                    mv.visitInsn(DUP)
                                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
                                    mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                    mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                    mv.visitTypeInsn(NEW, newClassName)
                                    mv.visitInsn(DUP)
                                    mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
                                    mv.visitInsn(POP)
                                } catch (e : Exception) {
                                    e.printStackTrace()
                                }
                            }
                            type.startsWith("[") -> {
                                // 如果是array [Lxxx;
                                var newClassName = type.substring(1)
                                println("new classes name : $newClassName")
                                if(newClassName.startsWith("L")) {
                                    newClassName = newClassName.substring(1).let { it.substring(0, it.length - 1) }
                                    mv.visitInsn(ICONST_1)
                                    mv.visitTypeInsn(ANEWARRAY, newClassName)
                                    mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                    when(newClassName) {
                                        "java/lang/Byte" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Short" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Integer" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Long" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Float" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Double" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/Character" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitIntInsn(BIPUSH, 48)
                                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                        "java/lang/String" -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitLdcInsn("")
                                            mv.visitInsn(AASTORE)
                                        }
                                        else -> {
                                            mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                                            mv.visitInsn(ICONST_0)
                                            mv.visitTypeInsn(NEW, newClassName)
                                            mv.visitInsn(DUP)
                                            mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                            mv.visitInsn(AASTORE)
                                        }
                                    }
                                } else {
                                    mv.visitInsn(ICONST_1)
                                    mv.visitIntInsn(NEWARRAY, when(newClassName) {
                                        "Z" -> T_BOOLEAN
                                        "B" -> T_BYTE
                                        "S" -> T_SHORT
                                        "I" -> T_INT
                                        "J" -> T_LONG
                                        "F" -> T_FLOAT
                                        "D" -> T_DOUBLE
                                        "C" -> T_CHAR
                                        else -> T_BYTE
                                    })
                                    mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                                }
                            }
                            else -> {
                                val newClassName = type.substring(1).let { it.substring(0, it.length - 1) }
                                mv.visitTypeInsn(NEW, newClassName)
                                mv.visitInsn(DUP)
                                mv.visitMethodInsn(INVOKESPECIAL, newClassName, "<init>", "()V", false)
                                mv.visitFieldInsn(PUTSTATIC, className, cacheName, type)
                            }
                        }
                        mv.visitLabel(gtLabel)
                        mv.visitFrame(F_SAME, 0, null, 0, null)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitVarInsn(ALOAD, 1)
                        mv.visitFieldInsn(GETSTATIC, className, cacheName, type)
                    } else {
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitVarInsn(ALOAD, 1)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitFieldInsn(GETFIELD, className, name, type)
                    }
                    mv.visitIntInsn(SIPUSH, id)
                    mv.visitInsn(if(field.require) ICONST_1 else ICONST_0)
                    mv.visitMethodInsn(INVOKEVIRTUAL, CLASS_TARS_INPUT, "read", if(field.isBaseType()) "(${type}IZ)${type}" else "(Ljava/lang/Object;IZ)Ljava/lang/Object;", false)
                    if(!field.isBaseType()) {
                        if(type.startsWith("[")) {
                            // 如果是 array就不能去掉L
                            mv.visitTypeInsn(CHECKCAST, type)
                        } else {
                            mv.visitTypeInsn(CHECKCAST, type.substring(1).let { it.substring(0, it.length - 1) })
                        }
                    }
                    mv.visitFieldInsn(PUTFIELD, className, name, type) // put
                }
            }
            mv.visitInsn(RETURN)
            mv.visitMaxs(5, 2)
            mv.visitEnd()
        })

        if(tarsClassInfo.servantName.isNotEmpty() && !hasMethod("servantName")) this.methods.add(MethodNode(ACC_PUBLIC, "servantName", "()Ljava/lang/String;", null, null).also { mv ->
            mv.visitCode()
            mv.visitLdcInsn(tarsClassInfo.servantName)
            mv.visitInsn(ARETURN)
            mv.visitMaxs(1, 1)
            mv.visitEnd()
        })

        if(tarsClassInfo.funcName.isNotEmpty() && !hasMethod("funcName")) this.methods.add(MethodNode(ACC_PUBLIC, "funcName", "()Ljava/lang/String;", null, null).also { mv ->
            mv.visitCode()
            mv.visitLdcInsn(tarsClassInfo.funcName)
            mv.visitInsn(ARETURN)
            mv.visitMaxs(1, 1)
            mv.visitEnd()
        })

        if(tarsClassInfo.reqName.isNotEmpty() && !hasMethod("reqName")) this.methods.add(MethodNode(ACC_PUBLIC, "reqName", "()Ljava/lang/String;", null, null).also { mv ->
            mv.visitCode()
            mv.visitLdcInsn(tarsClassInfo.reqName)
            mv.visitInsn(ARETURN)
            mv.visitMaxs(1, 1)
            mv.visitEnd()
        })

        if(tarsClassInfo.respName.isNotEmpty() && !hasMethod("respName")) this.methods.add(MethodNode(ACC_PUBLIC, "respName", "()Ljava/lang/String;", null, null).also { mv ->
            mv.visitCode()
            mv.visitLdcInsn(tarsClassInfo.respName)
            mv.visitInsn(ARETURN)
            mv.visitMaxs(1, 1)
            mv.visitEnd()
        })

        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        this.accept(cw)

        FileUtil.saveFile(clzFile.absolutePath, cw.toByteArray())
    }

    private fun doFirst() : ClassNode? {
        val clz = AsmUtil.bufToClassNode(bytes)
        println("do_first for ${clz.name}")
        if(clz.hasAnnotation(ANNOTATION_TARS_CLASS, true)) {
            println("Class[${clz.name}] is tars class.")
            this.className = clz.name
            clz.getAnnotation(ANNOTATION_TARS_CLASS, true)?.let {
                doClass(it)
                clz.visibleAnnotations.remove(it)
            }
            doFields(clz.fields)
            doMethod(clz.methods)
            return clz
        }
        return null
    }

    /**
     * 解析class
     */
    private fun doClass(classAnnotation : AnnotationNode) {
        tarsClassInfo.runCatching {
            val iterator = classAnnotation.values.iterator()
            while (iterator.hasNext()) {
                val k = iterator.next() as String
                val v = iterator.next()
                when(k) {
                    "requireRead" -> requireRead = v as Boolean
                    "requireWrite" -> requireWrite = v as Boolean
                    "servantName" -> servantName = v as String
                    "funcName" -> funcName = v as String
                    "reqName" -> reqName = v as String
                    "respName" -> respName = v as String
                    else -> error("unknown tars class annotation type : $k")
                }
            }
        }
    }

    /**
     * 解析field
     */
    private fun doFields(fields: List<FieldNode>?) {
        val fieldList = arrayListOf<FieldInfo>()
        fields?.forEach { field ->
            field.getAnnotation(ANNOTATION_TARS_FIELD, true)?.let { fieldAnnotation ->
                val iterator = fieldAnnotation.values.iterator()
                var id = 0
                var require = false
                var isEnum = false
                while (iterator.hasNext()) {
                    val k = iterator.next() as String
                    val v = iterator.next()
                    when(k) {
                        "id" -> id = v as Int
                        "require" -> require = v as Boolean
                        "isEnum" -> isEnum = v as Boolean
                        // "prepMethod" -> prepMethod = v as String
                        // code change
                        else -> error("unknown tars field annotation type : $k")
                    }
                }

                field.visibleAnnotations.remove(fieldAnnotation)

                fieldList.add(FieldInfo(
                    name = field.name,
                    id = id,
                    require = require,
                    type = field.desc,
                    sign = field.signature,
                    isEnum = isEnum
                ))
            }
        }

        TarsUtil.quickSort(fieldList).forEach {
            println(it)
            this.fields[it.id] = it
        }
    }

    /**
     * 解析方法
     */
    private fun doMethod(methods : List<MethodNode>?) {
        methods?.forEach { m ->
            m.getAnnotation(ANNOTATION_TARS_METHOD, true)?.let {
                val iterator = it.values.iterator()
                while (iterator.hasNext()) {
                    val k = iterator.next() as String
                    val v = iterator.next()
                    when(k) {
                        "fieldTag" -> fields[v]?.bind(MethodInfo(access = m.access, name = m.name, desc = m.desc))
                        else -> error("unknown tars field annotation type : $k")
                    }
                }

                m.visibleAnnotations.remove(it)
            }
        }
    }

    companion object {
        const val ANNOTATION_TARS_CLASS = "Lmoe/ore/tars/TarsClass;"
        const val ANNOTATION_TARS_FIELD = "Lmoe/ore/tars/TarsField;"
        const val ANNOTATION_TARS_METHOD = "Lmoe/ore/tars/TarsMethod;"

        const val CLASS_TARS_OUTPUT = "moe/ore/tars/TarsOutputStream"
        const val CLASS_TARS_INPUT = "moe/ore/tars/TarsInputStream"

        data class TarsClass(
            var requireRead: Boolean = false,
            var requireWrite : Boolean = false,
            var servantName : String = "",
            var funcName : String = "",
            var reqName : String = "",
            var respName : String = ""
        )
    }
}