package moe.ore.plugin

import moe.ore.plugin.full.ClassFuller
import moe.ore.plugin.util.AsmUtil.getAnnotation
import moe.ore.plugin.util.AsmUtil.hasAnnotation
import moe.ore.plugin.util.AsmUtil.hasMethod
import moe.ore.plugin.util.TarsUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
import java.io.File
import java.util.*

class TarsTransform(
    private val clzFile: File,
    private val clz: ClassFuller
) {
    private val fields : TreeMap<Int, FieldInfo> by lazy { TreeMap() }
    private val tarsClassInfo = TarsClass()
    private lateinit var className : String

    fun transform() {
        println("tars task result : " + doLast(doFirst()))
    }

    private fun doLast(clz : ClassFuller?) = clz?.runCatching {
        if (!hasMethod("setFieldByName") && !hasMethod("getFieldByName") && !hasMethod("containField"))
            TarsFieldWriter(this, this@TarsTransform.fields).invoke()

        if (tarsClassInfo.requireWrite && !hasMethod("writeTo"))
            TarsWriteWriter(this, this@TarsTransform.fields).invoke()

        if (tarsClassInfo.requireRead && !hasMethod("readFrom"))
            TarsReadWriter(this, this@TarsTransform.fields).invoke()

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

        clzFile.writeBytes(cw.toByteArray())
        // FileUtil.saveFile(clzFile.absolutePath, )
    }

    private fun doFirst() : ClassFuller? {
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
            var requireRead: Boolean = true,
            var requireWrite : Boolean = true,
            var servantName : String = "",
            var funcName : String = "",
            var reqName : String = "",
            var respName : String = ""
        )
    }
}