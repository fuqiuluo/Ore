package moe.ore.plugin

import moe.ore.plugin.full.ClassFuller
import moe.ore.plugin.full.CodeBuilder
import moe.ore.plugin.util.FileUtil
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
            // clz.methods.removeIf { it.name == "from" && it.desc == "([B)L$className;" } // remove method

            doFirst() // create make buf
            // doLast() // create parser method

            // ====== save to file
            val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
            clz.accept(cw)
            FileUtil.saveFile(clzFile.absolutePath, cw.toByteArray())
        }
    }

    private fun doLast() = clz.method(2, 2) {
        access = ACC_PUBLIC
        name = "from"
        args = arrayOf("[B")
        ret = "L$className;"

        code {
            initProtobufDefault()
            loadObject(1)
            invokeVirtual(CLASS_PROTOBUF_DEFAULT, "decodeFromByteArray", "(Lkotlinx/serialization/DeserializationStrategy;[B)Ljava/lang/Object;")
            cast(className)
            returnObject()
        }
    }

    private fun doFirst() = clz.method(3, 1) {
        access = ACC_PUBLIC
        name = "toByteArray"
        ret = "[B"
        code {
            // p0 == this
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