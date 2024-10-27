package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntValueInputDialog(
    title: String,
    value: Int,
    onDismiss: () -> Unit,
    onValueChanged: (Int) -> Unit
) {
    CustomDialog(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onDismiss = onDismiss,
    ) {
        TextField(
            value = "$value",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                val newValue = it.toIntOrNull() ?: 0
                onValueChanged(newValue)
            }, modifier = Modifier.fillMaxWidth()
        )
    }
}