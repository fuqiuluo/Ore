package moe.ore.tars

import moe.ore.tars.full.ClassFuller
import moe.ore.tars.util.AsmUtil
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import java.util.*

class TarsWriteWriter(
    private val fuller: ClassFuller,
    private val fields : TreeMap<Int, FieldInfo>
) {
    private val className = fuller.name // not have "L" and ";"

    fun invoke() = fuller.method(3, 2) {
        access = ACC_PUBLIC
        name = "writeTo"
        args = arrayOf("L${TarsTransform.CLASS_TARS_OUTPUT};")
        // ret = "V" default

        code {
            var startLabel = Label()
            var nextLabel = Label()
            fields.forEach { (tag, field) ->
                label(startLabel)
                frameSame()

                field.bindMethod.forEach { method ->
                    if(AsmUtil.isStatic(method.access)) {
                        invokeStatic(className, method.name, method.desc)
                    } else {
                        invokeVirtual(className, method.name, method.desc)
                    }
                }

                if(field.needCheckNull()) {
                    loadObject(0) // this
                    getField(className, field.name, field.type)
                    ifNull(nextLabel)
                }

                loadObject(1) // p1 (output)
                loadObject(0) // this

                getField(className, field.name, field.type)
                if(field.isEnum) {
                    invokeVirtual(field.type.let { it.substring(1, it.length - 1) }, "name", "()Ljava/lang/String;")
                }
                pushInt(tag)
                if(field.isBaseType()) {
                    invokeVirtual(TarsTransform.CLASS_TARS_OUTPUT, "write", "(${field.type}I)V")
                } else {
                    invokeVirtual(TarsTransform.CLASS_TARS_OUTPUT, "write", "(Ljava/lang/Object;I)V")
                }
                // ==== exchange label
                startLabel = nextLabel
                nextLabel = Label()
            }
            label(startLabel)
            frameSame()

            returnVoid()
        }
    }

}