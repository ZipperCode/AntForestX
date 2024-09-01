package org.zipper.ant.forest.xposed.ui.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.xposed.antforestx.core.util.ModuleHelper

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@RootNavGraph(start = true)
@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .height(200.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Done, contentDescription = "done", modifier = Modifier.padding(
                        end = 20.dp
                    )
                )

                Column {
                    Text(text = if (ModuleHelper.active()) "模块已激活" else "模块未激活", style = TextStyle(fontWeight = FontWeight.Bold))
                    Text(
                        text = "版本 = 1.0.0", style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                InfoMessage(title = "标题1", value = "内容")
                InfoMessage(title = "标题2", value = "内容")
                InfoMessage(title = "标题3", value = "内容")
                InfoMessage(title = "标题4", value = "内容")
            }
        }
    }
}


@Composable
fun InfoMessage(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = title, modifier = Modifier.weight(1f),
            lineHeight = 20.sp
        )
        Text(
            text = value,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End,
        )
    }
}