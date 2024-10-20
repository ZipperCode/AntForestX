package org.zipper.ant.forest.xposed.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(

) {
    ProfileContent(pages = ProfilePages.entries)
}

@Composable
fun ProfileContent(
    pages: List<ProfilePages>
) {
    val pagerState = rememberPagerState {
        pages.size
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage
        ) {
            pages.forEachIndexed { index, profilePages ->
                Tab(
                    text = { Text(profilePages.title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (pages[page]) {
                    ProfilePages.Basic -> {
                        ProfileBasicPageContent()
                    }

                    ProfilePages.Forest -> {
                        ProfileForestPageContent()
                    }

                    ProfilePages.Manor -> {
                        ProfileManorPageContent()
                    }

                    ProfilePages.Other -> {
                        ProfileOtherPageContent()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileBasicPageContent() {
    ProfileGroupCard(
        title = "插件配置"
    ) {
        ProfileSwitchRow("是否记录日志", true) {

        }
        ProfileSwitchRow("Toast提示", true) {

        }
        ProfileSwitchRow("任务并发", true) {

        }
        ProfileSwitchRow("状态栏禁删", true) {

        }
    }

}

@Composable
fun ProfileForestPageContent() {
    ProfileGroupCard(title = "收能量") {
        ProfileSwitchRow(title = "收集能量", checked = true) {

        }

        ProfileSwitchRow(title = "一键收取", checked = true) {

        }

        ProfileSwitchRow(title = "收取限制", checked = true) {

        }
        ProfileSwitchRow(title = "收好友能量", checked = true) {

        }
        ProfileSettingRow(title = "不收好友列表")

        ProfileSwitchRow(title = "帮助好友收取", checked = true) {

        }
        ProfileSettingRow(title = "不帮好友列表")



        ProfileSwitchRow(title = "能量雨", checked = true) {

        }



    }
    ProfileGroupCard(title = "浇水配置") {
        ProfileSwitchRow(title = "开启浇水", checked = true) {

        }
        ProfileSettingRow(title = "浇水配置")


        ProfileSwitchRow(title = "合种浇水", checked = true) {

        }
        ProfileSettingRow(title = "合种浇水数量")
    }
    ProfileGroupCard(title = "使用道具配置") {
        ProfileSwitchRow(title = "使用双击卡", checked = true) {

        }
        ProfileSettingRow(title = "双击卡使用时段")

        ProfileSwitchRow(title = "使用能量盾", checked = true) {

        }
        ProfileSwitchRow(title = "赠送好友道具", checked = true) {

        }
        ProfileSettingRow(title = "赠送好友列表")

        ProfileSwitchRow(title = "兑换双击卡", checked = true) {

        }
        ProfileSettingRow(title = "兑换数量")
        ProfileSwitchRow(title = "兑换能量盾", checked = true) {
        }
        ProfileSettingRow(title = "兑换能量盾数量")
    }
    ProfileGroupCard(title = "做任务配置") {
        ProfileSwitchRow(title = "开启森林任务", checked = true) {

        }

    }
    ProfileGroupCard(title = "海洋配置") {
        ProfileSwitchRow(title = "开启海洋", checked = true) {

        }
        ProfileSwitchRow(title = "捡垃圾", checked = true) {

        }
    }

}

@Composable
fun ProfileManorPageContent() {
    ProfileGroupCard(title = "蚂蚁庄园配置") {

    }
    ProfileGroupCard(title = "做任务配置") {

    }
    ProfileGroupCard(title = "农场配置") {

    }
}


@Composable
fun ProfileOtherPageContent() {
    ProfileGroupCard(title = "会员中心") {

    }
    ProfileGroupCard(title = "步数") {

    }
    ProfileGroupCard(title = "商家服务") {

    }
}


@Composable
fun ProfileSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(24.dp))
        Switch(
            checked = checked, onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun ProfileSettingRow(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileGroupCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            content()
        }
    }
}

@Preview
@Composable
fun ProfileSwitchRowPreview() {
    ProfileSwitchRow("", true) {

    }
}

enum class ProfilePages(
    val title: String
) {
    Basic("基础"),
    Forest("森林"),
    Manor("庄园"),
    Other("其他")
}
