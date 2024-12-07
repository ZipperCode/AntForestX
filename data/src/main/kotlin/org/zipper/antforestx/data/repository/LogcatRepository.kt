package org.zipper.antforestx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.zipper.antforestx.data.bean.FileBean
import org.zipper.antforestx.data.provider.StoreFileProvider
import java.io.File

internal class LogcatRepository : ILogcatRepository {

    override suspend fun getFileList(selectItemFile: File): Flow<List<FileBean>> {
        return flow {
            val rowFile = selectItemFile.listFiles()
                ?.filter { !it.isHidden && (it.isDirectory || (it.isFile && it.name.endsWith(".log"))) }
                ?.map {
                    if (it.isDirectory) {
                        FileBean.Folder(it)
                    } else {
                        FileBean.DocFile(it)
                    }
                } ?: emptyList()
            if (selectItemFile == StoreFileProvider.logcatRootDir) {
                emit(rowFile)
            } else {
                emit(listOf(selectItemFile.parentFile!!).map { f ->FileBean.TopFolder(f) } + rowFile)
            }

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun openFile(file: File): Flow<List<String>> {
        return flow<List<String>> {
            val result = mutableListOf<String>()
            file.forEachLine {
                result.add(it)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun readFileContent(file: File): List<String> {
        val result = mutableListOf<String>()
        file.forEachLine {
            result.add(it)
        }
        return result
    }
}