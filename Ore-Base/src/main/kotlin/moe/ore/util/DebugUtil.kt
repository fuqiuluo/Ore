package moe.ore.util

import java.net.InetSocketAddress

@SuppressWarnings("unchecked")
object DebugUtil {
    @JvmStatic
    fun getRunningThread() : Array<Thread> {
        return Thread.getAllStackTraces().keys.toTypedArray()
    }

    /**
     * 反射违规操作-强转
     *
     * @param source 目标类型
     * @param any Any 被强转对象
     */
    @JvmStatic
    fun <T> forcedConvert(source : T?, any: Any?) : T? {
        return any as T?
    }

    @JvmStatic
    fun <T> newInstance(clazz: Class<T>): T {
        return try {
            clazz.newInstance() as T
        } catch (e: Exception) {
            throw RuntimeException("instance Class: " + clazz.name + " with ex: " + e.message, e)
        }
    }

    @JvmStatic
    fun <T> newInstance(className: String): T {
        return try {
            Class.forName(className).newInstance() as T
        } catch (e: Exception) {
            throw RuntimeException("instance Class: " + className + " with ex: " + e.message, e)
        }
    }

    @JvmStatic
    fun forName(className: String): Class<*>? {
        return try {
            Class.forName(className)
        } catch (e: Exception) {
            throw RuntimeException("forName Class: " + className + " with ex: " + e.message, e)
        }
    }

    @JvmStatic
    fun isEmpty(input: String?): Boolean {
        return null == input || input.trim { it <= ' ' }.isEmpty()
    }

    @JvmStatic
    fun isEmptyArray(array: Array<Any?>?): Boolean {
        return null == array || array.isEmpty()
    }

    @JvmStatic
    fun isEmptyCollection(collection: Collection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    @JvmStatic
    fun isEmptyMap(map: Map<*, *>?): Boolean {
        return map == null || map.isEmpty()
    }

    @JvmStatic
    fun isJavaBase(clazz: Class<*>): Boolean {
        return clazz == Boolean::class.javaPrimitiveType || clazz == Boolean::class.java || clazz == Byte::class.javaPrimitiveType || clazz == Byte::class.java || clazz == Short::class.javaPrimitiveType || clazz == Short::class.java || clazz == Int::class.javaPrimitiveType || clazz == Int::class.java || clazz == Long::class.javaPrimitiveType || clazz == Long::class.java || clazz == Float::class.javaPrimitiveType || clazz == Float::class.java || clazz == Double::class.javaPrimitiveType || clazz == Double::class.java || clazz == String::class.java
    }

    /**
     * 获取Cpu线程池大小
     * @return Int
     */
    @JvmStatic
    fun getCpuThreadPoolSize(): Int {
        val processors = Runtime.getRuntime().availableProcessors()
        return if (processors > 8) 4 + processors * 5 / 8 else processors + 1
    }

    /**
     * 获取IO线程池大小
     * @return Int
     */
    @JvmStatic
    fun getIoThreadPoolSize(): Int {
        val processors = Runtime.getRuntime().availableProcessors()
        val dev = ((1 + 2) / 1) * processors
        return (processors * 2) + dev
    }

    /**
     * 获取混合线程池大小
     * @return Int
     */
    @JvmStatic
    fun getMixThreadPoolSize() = (getCpuThreadPoolSize() + getIoThreadPoolSize()) / 2

    @JvmStatic
    fun getIPAndPort(remoteAddress: InetSocketAddress): String {
        return String.format("%s:%s", remoteAddress.hostString, remoteAddress.port)
    }

    @JvmStatic
    fun convertMap(strMap: Map<String, String>): Map<String, Any> {
        val hashmap: MutableMap<String, Any> = HashMap()
        for ((key, value) in strMap) {
            hashmap[key] = value
        }
        return hashmap
    }
}