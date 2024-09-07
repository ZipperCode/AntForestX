package org.zipper.ant.forest.xposed.test

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.system.measureTimeMillis

class ColorNavigationBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {


    private val contentPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val sizeRectF = RectF()

    private val contentRectF = RectF()

    private val centerPoint = PointF()

    private val blankHeight = 100f
    private val smallCircleRadius = 70f
    private val centerCircleRadius = 70f

    private val path = Path()

    private val tabContentView: LinearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
    }

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.BOTTOM
        tabContentView.setHorizontalGravity(Gravity.CENTER)
        tabContentView.gravity = Gravity.CENTER
        addView(tabContentView, lp)

        val childLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        childLp.weight = 1f
        childLp.gravity = Gravity.CENTER

        tabContentView.addView(ColorNavigationBar(context), childLp)
        tabContentView.addView(ColorNavigationBar(context), childLp)
        tabContentView.addView(ColorNavigationBar(context), childLp)

        for (i in 0 until tabContentView.childCount) {
            val child = tabContentView.getChildAt(i) as ColorNavigationBar
            child.setOnClickListener {
                animCenter(child, (it.left + it.right) * 0.5f)

            }
        }
    }


    private fun animCenter(view: ColorNavigationBar, targetX: Float) {
        ValueAnimator.ofFloat(centerPoint.x, targetX).apply {
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                centerPoint.x = value
                invalidate()
            }
            duration = 250

            start()
        }
        for (i in 0 until tabContentView.childCount) {
            val child = tabContentView.getChildAt(i) as ColorNavigationBar
            child.setActive(child == view)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeRectF.set(0f, 0f, w.toFloat(), h.toFloat())
        centerPoint.set(w / 2f, blankHeight)

        contentRectF.set(0f, blankHeight, w.toFloat(), h.toFloat())
    }

    override fun dispatchDraw(canvas: Canvas) {
       val time =  measureTimeMillis {
            canvas.withClip {
                canvas.drawRect(contentRectF, contentPaint)
            }

            canvas.withClip(5) {
                canvas.drawRect(contentRectF, Paint().apply {
                    color = Color.parseColor("#00FF00")
                    style = Paint.Style.FILL
                })
            }

           canvas.withClip(10) {
               canvas.drawRect(contentRectF, Paint().apply {
                   color = Color.parseColor("#0000FF")
                   style = Paint.Style.FILL
               })
           }

            super.dispatchDraw(canvas)
        }
        Log.d("BAAA", "time = ${time}")
    }

    /**
     * 裁剪凹槽的内容
     */
    private inline fun Canvas.withClip(topOffset: Int = 0, crossinline block: () -> Unit) {
        val saveCount = save()

        val leftCircleCx = centerPoint.x - centerCircleRadius - smallCircleRadius
        val rightCircleCX = centerPoint.x + centerCircleRadius + smallCircleRadius

        val drawTop = blankHeight + topOffset

        path.reset()
        path.moveTo(0f, drawTop)
        path.lineTo(leftCircleCx, drawTop)

        path.apply {
            this.cubicTo(
                centerPoint.x - smallCircleRadius,
                drawTop,
                leftCircleCx + smallCircleRadius,
                centerPoint.y + centerCircleRadius + topOffset,
                centerPoint.x,
                centerPoint.y + centerCircleRadius + topOffset
            )

            this.cubicTo(
                centerPoint.x + centerCircleRadius,
                centerPoint.y + centerCircleRadius + topOffset,
                centerPoint.x + smallCircleRadius,
                drawTop,
                rightCircleCX,
                drawTop
            )

            lineTo(contentRectF.right, drawTop)
            lineTo(contentRectF.right, contentRectF.bottom)
            lineTo(0f, contentRectF.bottom)
            lineTo(0f, drawTop)
        }

        clipPath(path)
        block()
        restoreToCount(saveCount)
    }
}