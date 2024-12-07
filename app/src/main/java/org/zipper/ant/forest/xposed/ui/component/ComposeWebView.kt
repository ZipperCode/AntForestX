package org.zipper.ant.forest.xposed.ui.component

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ComposeWebView(
    modifier: Modifier = Modifier,
    url: String,
    onBack: WebView.() -> Unit,
    onWebViewCreated: WebView.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var webView: WebView? = null
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                onWebViewCreated()
                webView = this
                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        when (event) {
                            Lifecycle.Event.ON_PAUSE -> onPause()
                            Lifecycle.Event.ON_RESUME -> onResume()
                            Lifecycle.Event.ON_DESTROY -> {
                                source.lifecycle.removeObserver(this)
                            }

                            else -> Unit
                        }
                    }
                })
            }
        },
        modifier = modifier,
        update = {
            it.loadUrl(url)
        },
        onReset = {
            it.stopLoading()
            it.loadUrl("about:black")
            it.clearHistory()
        }
    )
    BackHandler {
        coroutineScope.launch {
            webView?.let(onBack)
        }
    }
}