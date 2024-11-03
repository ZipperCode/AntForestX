package org.zipper.ant.forest.xposed.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CommonRadioButtonItem(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    radioTags:List<String>,

) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) {
            label()
        }
        RadioButton(selected = false, onClick = { /*TODO*/ })
    }
}