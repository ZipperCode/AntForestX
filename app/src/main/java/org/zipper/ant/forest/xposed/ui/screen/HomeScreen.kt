package org.zipper.ant.forest.xposed.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.navigation.navigateToWebView
import org.zipper.ant.forest.xposed.ui.icon.AppIcons
import org.zipper.ant.forest.xposed.ui.icon.Github
import org.zipper.ant.forest.xposed.ui.util.LocalRootNavController
import org.zipper.ant.forest.xposed.viewmodel.AntDataViewModel


@Composable
fun HomeScreen(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HomeActivePanel(true)
        Spacer(modifier = Modifier.height(16.dp))
        HomeDetailPanel()
        Spacer(modifier = Modifier.height(16.dp))
        HomeGithubPanel()
    }
}

@Composable
private fun HomeActivePanel(
    active: Boolean,
    modifier: Modifier = Modifier,
    version: String = "1.0.0"
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        enabled = active,
        onClick = {}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.width(32.dp))
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "")
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                Text(
                    text = if (active) "已激活" else "未激活",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "版本: $version",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun HomeDetailPanel(
    dataViewModel: AntDataViewModel = koinViewModel()
) {

    val homeUiState by dataViewModel.homeUiState.collectAsStateWithLifecycle()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            InfoMessage("蚂蚁用户", "(${homeUiState.displayUser})")
            InfoMessage("好友数量", "${homeUiState.friendCount}个")
            InfoMessage("当日收取能量", "${homeUiState.dayCollectEnergy}g")
            InfoMessage("当日获得活力值", "${homeUiState.dayVitality}")
            InfoMessage("当日好友浇水", "${homeUiState.dayWatering}g")
            InfoMessage("当日助力能量", "${homeUiState.dayHelpEnergy}g")
            InfoMessage("当日步数", "${homeUiState.dayStepNum}")
        }
    }
}

@Composable
private fun HomeGithubPanel() {
    val rootNavController = LocalRootNavController.current
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = AppIcons.Github,
                    contentDescription = "Github",
                    modifier = Modifier.size(20.dp, 20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "开源地址", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF0E9FF2),
                    modifier = Modifier.clickable {
                        rootNavController.navigateToWebView(
                            "https://github.com/ZipperCode/AntForestX",
                            navOptions {
                                restoreState = true
                            })
                    }
                )
            }
        }
    }
}

@Composable
fun InfoMessage(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            lineHeight = 16.sp
        )
        Text(
            text = value,
            modifier = Modifier.weight(2f),
            fontSize = 14.sp,
            textAlign = TextAlign.End,
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}