package org.zipper.ant.forest.xposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

import org.zipper.ant.forest.xposed.ui.AppViewModel
import org.zipper.ant.forest.xposed.ui.presentation.BottomBarItem
import org.zipper.ant.forest.xposed.ui.presentation.NavGraphs
import org.zipper.ant.forest.xposed.ui.theme.AntForestXTheme
import org.zipper.ant.forest.xposed.ui.util.LocalAppViewModel
import org.zipper.ant.forest.xposed.ui.util.LocalNavController
import org.zipper.ant.forest.xposed.ui.util.LocalSnackbarHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainApp(appViewModel: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    AntForestXTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(
            LocalAppViewModel provides appViewModel,
            LocalSnackbarHost provides snackBarHostState,
            LocalNavController provides navController
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(text = "TopBar") },
                        modifier = Modifier,
                    )
                },
                bottomBar = { BottomBar(navController) },
                snackbarHost = { SnackbarHost(snackBarHostState) },
            ) { innerPadding ->
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    navController = navController
                )
            }
        }

    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    var selectBottomBar by remember {
        mutableStateOf(BottomBarItem.Home)
    }
    NavigationBar {
        BottomBarItem.entries.forEach { bar ->
            NavigationBarItem(selected = selectBottomBar == bar, onClick = {
                selectBottomBar = bar
                navController.navigate(bar.direction) {
                    popUpTo(NavGraphs.root) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, icon = {
                Icon(imageVector = bar.icon, contentDescription = bar.label)
            }, label = {
                Text(text = bar.label)
            })
        }
    }
}