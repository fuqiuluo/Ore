package moe.ore.plugin

import moe.ore.plugin.full.ClassFuller
import moe.ore.plugin.full.CodeBuilder
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import java.io.File

class ProtoBufTransform (
    private val clzFile: File,
    private val clz: ClassFuller
) {
    private val className = clz.name

    fun transform() {
        if (clz.interfaces.contains(CLASS_PROTOBUF_INTERFACE)) {
            clz.methods.removeIf { it.name == "toByteArray" && it.desc == "()[B" } // remove method
            doFirst()
            val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
            clz.accept(cw)

            clzFile.writeBytes(cw.toByteArray())
        }
    }

    private fun doFirst() = clz.method(3, 1) {
        access = ACC_PUBLIC
        name = "toByteArray"
        ret = "[B"
        code {
            initProtobufDefault()
            loadObject(0)
            invokeVirtual(CLASS_PROTOBUF_DEFAULT, "encodeToByteArray", "(Lkotlinx/serialization/SerializationStrategy;Ljava/lang/Object;)[B")
            returnObject()
        }
    }

    private fun CodeBuilder.initProtobufDefault() {
        getField(CLASS_PROTOBUF, "Default", "L$CLASS_PROTOBUF_DEFAULT;", true)
        getField(className, "Companion", "L$className\$Companion;", true)
        invokeVirtual("$className\$Companion", "serializer", "()Lkotlinx/serialization/KSerializer;")
        cast("kotlinx/serialization/SerializationStrategy")
    }

    companion object {
        const val CLASS_PROTOBUF = "kotlinx/serialization/protobuf/ProtoBuf"
        const val CLASS_PROTOBUF_DEFAULT = "$CLASS_PROTOBUF\$Default"
        const val CLASS_PROTOBUF_INTERFACE = "moe/ore/protobuf/Protobuf"
    }
}