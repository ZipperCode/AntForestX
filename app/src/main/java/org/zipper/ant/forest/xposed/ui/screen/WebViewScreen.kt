package org.zipper.ant.forest.xposed.ui.screen

import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zipper.ant.forest.xposed.ui.component.ComposeWebView
import org.zipper.ant.forest.xposed.ui.util.LocalRootNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String
) {
    val rootNavController = LocalRootNavController.current
    var webTitle by remember {
        mutableStateOf("")
    }

    var rememberWebProgress: Int by remember { mutableIntStateOf(-1) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = webTitle,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        rootNavController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ComposeWebView(
                modifier = Modifier
                    .fillMaxSize(),
                url = url,
                onBack = {
                    if (canGoBack()) {
                        goBack()
                    } else {
                        rootNavController.popBackStack()
                    }
                },
                onWebViewCreated = {
                    this.webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            view?.let {
                                webTitle = it.title ?: ""
                            }
                        }
                    }
                    this.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            rememberWebProgress = newProgress
                        }
                    }
                }
            )
            LinearProgressIndicator(
                progress = {
                    rememberWebProgress * 1.0F / 100F
                },
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (rememberWebProgress == 100) 0.dp else 3.dp)
            )
        }
    }
}