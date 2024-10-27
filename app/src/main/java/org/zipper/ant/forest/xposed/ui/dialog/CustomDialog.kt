package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.zipper.ant.forest.xposed.R


@Composable
fun CustomDialog(
    title: @Composable (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 160.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = title,
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                content()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.app_dialog_confirm_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss?.invoke() },
            )
        },
    )
}