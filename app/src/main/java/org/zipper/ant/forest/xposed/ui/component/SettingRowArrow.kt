package org.zipper.ant.forest.xposed.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zipper.ant.forest.xposed.ui.icon.AppIcons
import org.zipper.ant.forest.xposed.ui.icon.Arrow_forward_ios

@Composable
fun SettingRowArrow(
    modifier: Modifier = Modifier,
    title: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Icon(AppIcons.Arrow_forward_ios, "arrow", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(5.dp))
    }
}
