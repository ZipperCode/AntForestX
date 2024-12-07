package org.zipper.ant.forest.xposed.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.ui.icon.AppIcons
import org.zipper.ant.forest.xposed.ui.icon.File
import org.zipper.ant.forest.xposed.ui.icon.FileSubmodule
import org.zipper.ant.forest.xposed.ui.theme.GreenGray90
import org.zipper.ant.forest.xposed.viewmodel.AntDataViewModel
import org.zipper.antforestx.data.bean.FileBean


@Composable
fun LogcatScreen(
    antDataViewModel: AntDataViewModel = koinViewModel()
) {
    val fileList by antDataViewModel.fileList.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(true) {
            antDataViewModel.loadFileList()
        }
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items = fileList) { file ->
                Column {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                if (file is FileBean.Folder || file is FileBean.TopFolder) {
                                    antDataViewModel.getFileList(file.file)
                                } else {
                                    antDataViewModel.openFile(file.file)
                                }
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        if (file is FileBean.Folder || file is FileBean.TopFolder) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = AppIcons.FileSubmodule,
                                contentDescription = "Directory"
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = AppIcons.File,
                                contentDescription = "File"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (file is FileBean.TopFolder) {
                            Text(text = "...")
                        } else {
                            Text(text = file.file.name)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }

        val openFileState by antDataViewModel.openFileState
        if (openFileState) {
            val fileContent by antDataViewModel.fileContent
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(fileContent){
                        Text(text = it, fontSize = 14.sp)
                        HorizontalDivider()
                    }
                }

                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(GreenGray90)
                        .clickable(onClick = antDataViewModel::closeOpenFile),
                    imageVector = Icons.Filled.Close,
                    contentDescription = ""
                )
            }
        }

    }
}