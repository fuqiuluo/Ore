package moe.ore.plugin

import moe.ore.plugin.TarsTransform.Companion.CLASS_TARS_INPUT
import moe.ore.plugin.full.ClassFuller
import moe.ore.plugin.full.CodeBuilder
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import java.util.*

class TarsReadWriter(
    private val fuller: ClassFuller,
    private val fields : TreeMap<Int, FieldInfo>
) {
    private val className = fuller.name // not have "L" and ";"

    fun invoke() = with(fuller) {
        method(7, 7) {
            access = ACC_PUBLIC
            name = "readFrom"
            args = arrayOf("L$CLASS_TARS_INPUT;")

            code {
                this@TarsReadWriter.fields.forEach { (tag, field) ->
                    if(field.isEnum) {
                        val nextLabel = Label()
                        loadObject(1) // p1 (input)
                        pushInt(tag)
                        pushBoolean(field.require)
                        invokeVirtual(CLASS_TARS_INPUT, "readString", "(IZ)Ljava/lang/String;")
                        // p0 p1 被占用 --> 使用p2
                        storeObject(2) // save result

                        loadObject(2)
                        ifNull(nextLabel)

                        loadObject(0) // for put field

                        loadObject(2) // string value
                        invokeStatic(field.type.let { it.substring(1, it.length - 1) }, "valueOf", "(Ljava/lang/String;)${field.type}")

                        putField(className, field.name, field.type)

                        label(nextLabel)
                        // frameAppend(1, arrayOf("java/lang/String"))
                        frameSame()
                    }
                    else {

                        val fieldType = field.type
                        if(field.isTarsObject()) {
                            val codeLabel = Label()
                            val cacheName = "cache_${field.name}"
                            field(ACC_PRIVATE + ACC_STATIC, cacheName, fieldType, field.sign)
                            getField(className, cacheName, fieldType, true)
                            ifNoNull(codeLabel)
                            when {
                                fieldType == "Ljava/util/HashMap;" || fieldType == "Ljava/util/Map;" -> {
                                    // Ljava/util/HashMap<Ljava/lang/Byte;LTarsTest$Objsua;>;
                                    val allType = field.sign!!.substring(fieldType.length).let { it.substring(0, it.length - 2) }.split(";")
                                    // 0 key 1 value 2 empty
                                    val keyType = allType[0].substring(1)
                                    val newClassName = allType[1].substring(1)
                                    newInstance("java/util/HashMap", "()V")
                                    storeObject(2)

                                    loadObject(2)
                                    forPut(keyType) // key
                                    forPut(newClassName) // value
                                    invokeInterface("java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
                                    pop() // 弹出put操作的返回值

                                    loadObject(2)
                                    putField(className, cacheName, fieldType, true)

                                    // getField(className, cacheName, fieldType, true) // for put value
                                }
                                fieldType == "Ljava/util/ArrayList;" || fieldType == "Ljava/util/List;" -> {
                                    // Ljava/util/ArrayList<Lxxx;>;
                                    val valueType = field.sign!!.substring(fieldType.length).let { it.substring(0, it.length - 2) }
                                    // Lxxx;
                                    val newClassName = valueType.substring(1).let { it.substring(0, it.length - 1) }
                                    // list sign 内一定是对象 则不可能是 baseValue

                                    newInstance("java/util/ArrayList", "()V")
                                    storeObject(2) // save to p2

                                    loadObject(2)
                                    forPut(newClassName) // value
                                    invokeInterface("java/util/List", "add", "(Ljava/lang/Object;)Z")
                                    pop() // pop add result

                                    loadObject(2)
                                    putField(className, cacheName, fieldType, true)
                                }
                                fieldType.startsWith("[") -> {
                                    // if is array then type = “[Lxxx; or [B”
                                    var newClassName = fieldType.substring(1)
                                    if(newClassName.startsWith("L")) {
                                        newClassName = newClassName.substring(1).let { it.substring(0, it.length - 1) } // remove "L" and ";"
                                        newArray(newClassName, 1)
                                        storeObject(2) // save to p2

                                        loadObject(2)
                                        constN0() // array index
                                        forPut(newClassName)
                                        storeObjectTA()

                                        loadObject(2)
                                    } else newArray(newClassName, 1)
                                    putField(className, cacheName, fieldType, true)
                                }
                                else -> {
                                    newInstance(fieldType.substring(1).let { it.substring(0, it.length - 1) }, "()V") // remove "L" and ";"
                                    putField(className, cacheName, fieldType, true)
                                }
                            }
                            label(codeLabel)
                            frameSame()

                            // frameAppend(1, arrayOf("java/lang/String")) not to do, will wrong

                            loadObject(0) // p0 ,for put field
                            loadObject(1) // p1 ,for read
                            getField(className, cacheName, fieldType, true)
                        } else {
                            loadObject(0) // p0 ,for put field
                            loadObject(1) // p1 ,for read
                            loadObject(0) // for get field
                            getField(className, field.name, fieldType)
                        }

                        pushInt(tag)
                        pushBoolean(field.require)
                        invokeVirtual(CLASS_TARS_INPUT, "read", if(field.isBaseType()) "(${fieldType}IZ)${fieldType}" else "(Ljava/lang/Object;IZ)Ljava/lang/Object;")
                        if(!field.isBaseType()) {
                            if(fieldType.startsWith("[")) {
                                // 如果是 array就不能去掉L
                                cast(fieldType)
                            } else {
                                cast(fieldType.substring(1).let { it.substring(0, it.length - 1)})
                            }
                        }
                        putField(className, field.name, fieldType)
                    }
                }
                returnVoid()
            }

        }
    }

    private fun CodeBuilder.forPut(type : String) {
        when(type) {
            "java/lang/Byte" -> {
                constN0()
                invokeStatic("java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;")
            }
            "java/lang/Short" -> {
                constN0()
                invokeStatic("java/lang/Short", "valueOf", "(S)Ljava/lang/Short;")
            }
            "java/lang/Integer" -> {
                constN0()
                invokeStatic("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")
            }
            "java/lang/Long" -> {
                constN0()
                invokeStatic("java/lang/Long", "valueOf", "(J)Ljava/lang/Long;")
            }
            "java/lang/Float" -> {
                constN0()
                invokeStatic("java/lang/Float", "valueOf", "(F)Ljava/lang/Float;")
            }
            "java/lang/Double" -> {
                constN0()
                invokeStatic("java/lang/Double", "valueOf", "(D)Ljava/lang/Double;")
            }
            "java/lang/Character" -> {
                constN0()
                invokeStatic("java/lang/Character", "valueOf", "(C)Ljava/lang/Character;")
            }
            "java/lang/String" -> ldc("")
            else -> newInstance(type, "()V")
        }
    }

}