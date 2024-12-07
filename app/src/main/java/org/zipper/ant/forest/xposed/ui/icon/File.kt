package org.zipper.ant.forest.xposed.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val AppIcons.File: ImageVector
	get() {
		if (_File != null) {
			return _File!!
		}
		_File = ImageVector.Builder(
            name = "File",
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
				moveTo(13.71f, 4.29f)
				lineToRelative(-3f, -3f)
				lineTo(10f, 1f)
				horizontalLineTo(4f)
				lineTo(3f, 2f)
				verticalLineToRelative(12f)
				lineToRelative(1f, 1f)
				horizontalLineToRelative(9f)
				lineToRelative(1f, -1f)
				verticalLineTo(5f)
				lineToRelative(-0.29f, -0.71f)
				close()
				moveTo(13f, 14f)
				horizontalLineTo(4f)
				verticalLineTo(2f)
				horizontalLineToRelative(5f)
				verticalLineToRelative(4f)
				horizontalLineToRelative(4f)
				verticalLineToRelative(8f)
				close()
				moveToRelative(-3f, -9f)
				verticalLineTo(2f)
				lineToRelative(3f, 3f)
				horizontalLineToRelative(-3f)
				close()
			}
		}.build()
		return _File!!
	}

private var _File: ImageVector? = null
