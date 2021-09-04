package moe.ore.highway.util

import java.io.File
import java.io.RandomAccessFile

object ImageUtil {
    fun getImageType(bytes: ByteArray): ImageType {
        return when(bytes[0].toInt()) {
            0x89 -> { // png
                ImageType.PNG
            }
            0xff -> { // jpg
                ImageType.JPG
            }
            0x47 -> {
                ImageType.GIF
            }
            else -> error("unknown image type")
        }
    }

    fun getImageType(file: File): ImageType {
        val type: ImageType
        RandomAccessFile(file, "r").use {
            type = when(it.read()) {
                0x89 -> { // png
                    ImageType.PNG
                }
                0xff -> { // jpg
                    ImageType.JPG
                }
                0x47 -> {
                    ImageType.GIF
                }
                else -> error("unknown image type")
            }
        }
        return type
    }

}

enum class ImageType {
    PNG,
    JPG,
    GIF
}