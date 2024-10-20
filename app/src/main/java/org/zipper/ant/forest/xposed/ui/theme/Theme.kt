package org.zipper.ant.forest.xposed.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66BB6A),      // 主色 - 绿色
    onPrimary = Color(0xFF1B5E20),    // 主色上方内容颜色 - 深绿
    primaryContainer = Color(0xFF2E7D32), // 主色容器颜色 - 更深的绿
    onPrimaryContainer = Color(0xFFC8E6C9), // 主色容器上方内容颜色 - 浅绿
    secondary = Color(0xFF9CCC65),    // 次色 - 浅绿
    onSecondary = Color(0xFFFFFFFF),  // 次色上方内容颜色 - 白色
    secondaryContainer = Color(0xFF558B2F), // 次色容器颜色 - 深绿
    onSecondaryContainer = Color(0xFFDCEDC8), // 次色容器上方内容颜色 - 浅绿
    tertiary = Color(0xFF7CB342),     // 第三色 - 中等绿色
    onTertiary = Color(0xFFFFFFFF),   // 第三色上方内容颜色 - 白色
    tertiaryContainer = Color(0xFF558B2F), // 第三色容器颜色 - 深绿
    onTertiaryContainer = Color(0xFFA5D6A7), // 第三色容器上方内容颜色 - 浅绿
    background = Color(0xFF1B5E20),   // 背景色 - 深绿
    onBackground = Color(0xFFFFFFFF), // 背景上方内容颜色 - 白色
    surface = Color(0xFF1B5E20),      // 表面颜色 - 深绿
    onSurface = Color(0xFFFFFFFF),    // 表面上方内容颜色 - 白色
    surfaceVariant = Color(0xFF2E7D32), // 表面变体颜色 - 深绿
    onSurfaceVariant = Color(0xFFA5D6A7), // 表面变体上方内容颜色 - 浅绿
    error = Color(0xFFEF9A9A),        // 错误颜色 - 浅红色
    onError = Color(0xFFC62828),      // 错误上方内容颜色 - 深红色
    errorContainer = Color(0xFFD32F2F), // 错误容器颜色 - 红色
    onErrorContainer = Color(0xFFEF9A9A), // 错误容器上方内容颜色 - 浅红色
    outline = Color(0xFF66BB6A)       // 轮廓颜色 - 绿色
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),      // 主色 - 绿色
    onPrimary = Color(0xFFFFFFFF),    // 主色上方内容颜色 - 白色
    primaryContainer = Color(0xFFC8E6C9), // 主色容器颜色 - 浅绿色
    onPrimaryContainer = Color(0xFF1B5E20), // 主色容器上方内容颜色 - 深绿色
    secondary = Color(0xFF8BC34A),    // 次色 - 浅绿
    onSecondary = Color(0xFF000000),  // 次色上方内容颜色 - 黑色
    secondaryContainer = Color(0xFFDCEDC8), // 次色容器颜色 - 更浅的绿
    onSecondaryContainer = Color(0xFF33691E), // 次色容器上方内容颜色 - 深绿
    tertiary = Color(0xFF689F38),     // 第三色 - 中等绿色
    onTertiary = Color(0xFFFFFFFF),   // 第三色上方内容颜色 - 白色
    tertiaryContainer = Color(0xFFA5D6A7), // 第三色容器颜色 - 柔和的绿色
    onTertiaryContainer = Color(0xFF1B5E20), // 第三色容器上方内容颜色 - 深绿
    background = Color(0xFFF1F8E9),   // 背景色 - 非常浅的绿色
    onBackground = Color(0xFF000000), // 背景上方内容颜色 - 黑色
    surface = Color(0xFFFFFFFF),      // 表面颜色 - 白色
    onSurface = Color(0xFF000000),    // 表面上方内容颜色 - 黑色
    surfaceVariant = Color(0xFFC5E1A5), // 表面变体颜色 - 浅绿色
    onSurfaceVariant = Color(0xFF000000), // 表面变体上方内容颜色 - 黑色
    error = Color(0xFFD32F2F),        // 错误颜色 - 红色
    onError = Color(0xFFFFFFFF),      // 错误上方内容颜色 - 白色
    errorContainer = Color(0xFFEF9A9A), // 错误容器颜色 - 浅红色
    onErrorContainer = Color(0xFFB00020), // 错误容器上方内容颜色 - 深红色
    outline = Color(0xFF4CAF50)       // 轮廓颜色 - 绿色
)

/**
 * Light default theme color scheme
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,                                     // 主色
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray20,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50,
)

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

/**
 * Light Android theme color scheme
 */
@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

/**
 * Dark Android theme color scheme
 */
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

@Composable
fun AntForestXTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S