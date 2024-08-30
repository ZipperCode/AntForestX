package org.zipper.ant.forest.xposed.ui.presentation.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */

@RootNavGraph
@Destination
@Composable
fun ProfileScreen() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "ProfileScreen")
    }
}