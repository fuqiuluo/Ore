package moe.ore.tars

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9

object CompileTars {

    fun compileDir(input : ByteArray) : ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val cr = ClassReader(input)

        val cv = object : ClassVisitor(ASM9, cw) {
            override fun visitMethod(
                access: Int,
                name: String,
                descriptor: String?,
                signature: String?,
                exceptions: Array<out String>?
            ): MethodVisitor {
                // 添加对类的内方法的控制
                return object : MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    override fun visitVarInsn(opcode: Int, `var`: Int) {
                        super.visitVarInsn(opcode, `var`)
                    }

                    override fun visitLdcInsn(value: Any?) {
                        if(value is String && value.contains("伏秋洛")) {
                            super.visitLdcInsn(value.replace("伏秋洛", "上官婉儿"))
                        } else {
                            super.visitLdcInsn(value)
                        }
                    }
                }
            }

            override fun visitEnd() {
                super.visitEnd()
            }
        }

        cr.accept(cv, 0) // 将cr代理给cv

        return cw.toByteArray()
    }


}