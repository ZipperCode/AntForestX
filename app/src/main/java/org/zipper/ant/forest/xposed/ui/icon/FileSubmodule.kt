package org.zipper.ant.forest.xposed.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val AppIcons.FileSubmodule: ImageVector
	get() {
		if (_FileSubmodule != null) {
			return _FileSubmodule!!
		}
		_FileSubmodule = ImageVector.Builder(
            name = "FileSubmodule",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFF000000)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.EvenOdd
			) {
				moveTo(2f, 11f)
				horizontalLineToRelative(1f)
				verticalLineTo(6.99f)
				horizontalLineTo(2f)
				verticalLineTo(11f)
				close()
				moveToRelative(1f, -5.01f)
				verticalLineTo(5.5f)
				lineToRelative(0.5f, -0.5f)
				horizontalLineToRelative(4.43f)
				lineToRelative(0.43f, 0.25f)
				lineToRelative(0.43f, 0.75f)
				horizontalLineToRelative(5.71f)
				lineToRelative(0.5f, 0.5f)
				verticalLineToRelative(8f)
				lineToRelative(-0.5f, 0.5f)
				horizontalLineToRelative(-11f)
				lineToRelative(-0.5f, -0.5f)
				verticalLineTo(12f)
				horizontalLineTo(1.5f)
				lineToRelative(-0.5f, -0.5f)
				verticalLineToRelative(-9f)
				lineToRelative(0.5f, -0.5f)
				horizontalLineToRelative(4.42f)
				lineToRelative(0.44f, 0.25f)
				lineToRelative(0.43f, 0.75f)
				horizontalLineToRelative(5.71f)
				lineToRelative(0.5f, 0.5f)
				verticalLineTo(6f)
				lineToRelative(-1f, -0.03f)
				verticalLineTo(4f)
				horizontalLineTo(6.5f)
				lineToRelative(-0.43f, -0.25f)
				lineTo(5.64f, 3f)
				horizontalLineTo(2f)
				verticalLineToRelative(2.99f)
				horizontalLineToRelative(1f)
				close()
				moveToRelative(5.07f, 0.76f)
				lineTo(7.64f, 6f)
				horizontalLineTo(4f)
				verticalLineToRelative(3f)
				horizontalLineToRelative(3.15f)
				lineToRelative(0.41f, -0.74f)
				lineTo(8f, 8f)
				horizontalLineToRelative(6f)
				verticalLineTo(7f)
				horizontalLineTo(8.5f)
				lineToRelative(-0.43f, -0.25f)
				close()
				moveTo(7.45f, 10f)
				horizontalLineTo(4f)
				verticalLineToRelative(4f)
				horizontalLineToRelative(10f)
				verticalLineTo(9f)
				horizontalLineTo(8.3f)
				lineToRelative(-0.41f, 0.74f)
				lineToRelative(-0.44f, 0.26f)
				close()
			}
		}.build()
		return _FileSubmodule!!
	}

private var _FileSubmodule: ImageVector? = null
