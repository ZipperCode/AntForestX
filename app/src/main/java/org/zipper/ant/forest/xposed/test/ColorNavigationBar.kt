package org.zipper.ant.forest.xposed.test

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class ColorNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val content: TextView = TextView(context)

    val bigContent = TextView(context)


    init {
        val lp = LayoutParams(100, 100)
        content.background = GradientDrawable().apply {
            setColor(Color.parseColor("#FF0000"))
        }
        lp.gravity = Gravity.BOTTOM or Gravity.CENTER
        addView(content, lp)
    }

    fun setActive(active: Boolean) {
        var startTrans = 0f
        var endTranslate = -60f
        if (!active) {
            startTrans = content.translationY
            endTranslate = 0f
        }

        ObjectAnimator.ofFloat(startTrans, endTranslate).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = 250
            addUpdateListener {
                val value = it.animatedValue as Float
                content.translationY = value
            }

            doOnEnd {
                content.background = GradientDrawable().apply {
                    setColor(Color.parseColor(if (active) "#FF0000" else "#00FF00"))
                }

            }

            start()
        }

    }
}