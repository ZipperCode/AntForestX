package org.zipper.ant.forest.xposed.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntScaffold(
    topBarTitle: String? = null,
    content: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if (!topBarTitle.isNullOrEmpty()) {
                        Text(text = topBarTitle)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(

                )
            )
        },

        content = { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize())
        }
    )
}