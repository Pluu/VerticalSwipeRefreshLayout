package com.pluu.verticalswiperefreshlayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.core.content.res.use
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pluu.verticalswiperefreshlayout.sample.R
import logcat.logcat
import kotlin.math.abs
import kotlin.math.atan2

class VerticalSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    private var touchSlop = 0

    private var startX = 0f
    private var startY = 0f
    private var isDeclined = false
    private var checkDegree = DEFAULT_DEGREE
    private var halfDegree = checkDegree / 2f

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        loadAttributes(context, attrs)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.VerticalSwipeRefreshLayout).use { a ->
            val degree = a.getString(
                R.styleable.VerticalSwipeRefreshLayout_degree
            )?.toFloatOrNull() ?: DEFAULT_DEGREE
            setDegree(degree)
        }
    }

    @SuppressLint("Recycle")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = MotionEvent.obtain(event).x
                startY = MotionEvent.obtain(event).y
                isDeclined = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDeclined) return super.dispatchTouchEvent(event)
                val xDiff = abs(event.x - startX)
                val yDiff = abs(event.y - startY)
                if (xDiff > touchSlop || yDiff > touchSlop) {
                    val degree = toDegree(
                        startX, startY, event.x, event.y
                    ) + halfDegree
                    logcat { ">> $degree" }

                    if (degree in 0f..checkDegree) {
                        isDeclined = true
                        return super.dispatchTouchEvent(event)
                    }
                    return false
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                isDeclined = false
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun toDegree(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.toDegrees(
            atan2(x2 - x1, y2 - y1).toDouble()
        ).toFloat()
    }

    fun setDegree(degree: Float) {
        this.checkDegree = degree
        this.halfDegree = degree.div(2f)
    }

    companion object {
        private const val DEFAULT_DEGREE = 90f
    }

}