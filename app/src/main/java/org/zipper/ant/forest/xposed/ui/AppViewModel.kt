package org.zipper.ant.forest.xposed.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.zipper.ant.forest.xposed.ui.presentation.BottomBarItem

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */
class AppViewModel : ViewModel() {

    val selectBottomBar = MutableStateFlow(BottomBarItem.Home)
}