package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntValueInputDialog(
    title: String,
    value: Int,
    onDismiss: (Int) -> Unit,
) {

    var state by remember {
        mutableIntStateOf(value)
    }

    CustomDialog(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onDismiss = {
            onDismiss(state)
        },
    ) {
        TextField(
            value = "$value",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                state = it.toIntOrNull() ?: 0
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StringValueInputDialog(
    title: String,
    value: String,
    onDismiss: (String) -> Unit
) {
    var state by remember {
        mutableStateOf(value)
    }
    CustomDialog(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onDismiss = {
            onDismiss(state)
        },
    ) {
        TextField(
            value = value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                state = it
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RatioGroupDialog(
    modifier: Modifier = Modifier,
    title: String,
) {
    CustomDialog(
        title = title
    ) {

    }
}