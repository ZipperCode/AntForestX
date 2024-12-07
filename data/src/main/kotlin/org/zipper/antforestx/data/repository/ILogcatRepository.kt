package org.zipper.antforestx.data.repository

import kotlinx.coroutines.flow.Flow
import org.zipper.antforestx.data.bean.FileBean
import java.io.File

interface ILogcatRepository {

    suspend fun getFileList(selectItemFile: File): Flow<List<FileBean>>

    suspend fun openFile(file: File): Flow<List<String>>

    suspend fun readFileContent(file: File): List<String>
}