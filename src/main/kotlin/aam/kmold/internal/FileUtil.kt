package aam.kmold.internal

import java.io.File
import java.io.RandomAccessFile

internal fun endsWithNewLine(file: File): Boolean {
    RandomAccessFile(file, "r").use { fileHandler ->
        val fileLength = fileHandler.length() - 1
        if (fileLength < 0) {
            return true
        }
        fileHandler.seek(fileLength)
        val readByte = fileHandler.readByte()
        return readByte.toInt() == 0xA || readByte.toInt() == 0xD
    }
}
