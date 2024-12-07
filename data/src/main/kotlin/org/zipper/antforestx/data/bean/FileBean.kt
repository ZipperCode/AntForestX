package org.zipper.antforestx.data.bean

import java.io.File

sealed class FileBean(
    val file: File
) {

    class Folder(file: File) : FileBean(file)

    class TopFolder(file: File) : FileBean(file)

    class DocFile(file: File) : FileBean(file)
}