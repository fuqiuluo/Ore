package moe.ore.util

import moe.ore.helper.log.TLog
import kotlin.Throws
import java.io.*

object FileUtil {
    private val logger = TLog.getLogger("FileUtil")

    @JvmStatic
    fun has(string: String) = File(string).exists()

    @Throws(IOException::class)
    @JvmStatic
    fun readFile(f: File): ByteArray {
        return readFile(f.absolutePath)
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFile(filename: String) : ByteArray {
        return readFileBytes(filename)
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileString(f: File): String {
        return String(readFile(f))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFileString(path: String): String {
        return String(readFileBytes(path))
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileBytes(path: String): ByteArray {
        return readFileBytes(FileInputStream(path))
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileBytes(inputStream: InputStream): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        while (true) {
            val read = inputStream.read()
            if (read == -1) {
                inputStream.close()
                val byteArray = byteArrayOutputStream.toByteArray()
                byteArrayOutputStream.close()
                return byteArray
            }
            byteArrayOutputStream.write(read)
        }
    }

    @Throws(IOException::class)
    @JvmStatic
    fun saveFile(path: String, content: String) {
        saveFile(path, content.toByteArray())
    }

    @Throws(IOException::class)
    @JvmStatic
    fun saveFile(path: String, content: ByteArray) {
        val file = File(path)
        if (!file.exists()) {
            checkParentFile(file.parentFile)
            if (!file.createNewFile()) {
                logger.warn(String.format("[%S] File creation failed!", path))
                return
            }
        }
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(content)
        fileOutputStream.close()
    }

    private fun checkParentFile(file: File) {
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}