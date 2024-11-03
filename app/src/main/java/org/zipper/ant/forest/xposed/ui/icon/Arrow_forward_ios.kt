package org.zipper.ant.forest.xposed.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val AppIcons.Arrow_forward_ios: ImageVector
    get() {
        if (_Arrow_forward_ios != null) {
            return _Arrow_forward_ios!!
        }
        _Arrow_forward_ios = ImageVector.Builder(
            name = "Arrow_forward_ios",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(321f, 880f)
                lineToRelative(-71f, -71f)
                lineToRelative(329f, -329f)
                lineToRelative(-329f, -329f)
                lineToRelative(71f, -71f)
                lineToRelative(400f, 400f)
                close()
            }
        }.build()
        return _Arrow_forward_ios!!
    }

private var _Arrow_forward_ios: ImageVector? = null
