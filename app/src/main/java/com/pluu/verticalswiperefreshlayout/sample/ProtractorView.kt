package com.pluu.verticalswiperefreshlayout.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import kotlin.math.min

class ProtractorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val arcPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val count = 36
    private val mRotation = 360.0f / count
    private val rect = RectF()
    private var checkRotation = 0f

    init {
        paint.color = Color.parseColor("#99666666")
        paint.strokeWidth = 1f

        arcPaint.color = Color.CYAN

        context.obtainStyledAttributes(attrs, R.styleable.ProtractorView).use { a ->
            checkRotation = a.getString(0)?.toFloatOrNull() ?: 0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (width > height) super.onMeasure(
            heightMeasureSpec,
            heightMeasureSpec
        ) else super.onMeasure(widthMeasureSpec, widthMeasureSpec)

        updateDimensions(getWidth(), getHeight())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDimensions(w, h)
    }

    private fun updateDimensions(w: Int, h: Int) {
        // Update center position
        mCenterX = w / 2.0f
        mCenterY = h / 2.0f
        val archSize = min(w, h).toFloat().div(2)
        rect.set(mCenterX - archSize, mCenterY - archSize, mCenterX + archSize, mCenterY + archSize)
    }

    override fun onDraw(canvas: Canvas) {
        if (checkRotation > 0) {
            canvas.save()
            canvas.drawArc(rect, 90f - checkRotation.div(2), checkRotation, true, arcPaint)
            canvas.restore()
        }

        // Center our canvas
        canvas.save()
        canvas.translate(mCenterX, mCenterY)
        val stopY = height / 2f
        for (i in 0 until count) {
            canvas.save()
            canvas.rotate(i * mRotation)
            canvas.drawLine(0f, 0f, 0f, stopY, paint)
            canvas.restore()
        }
        canvas.restore()
        super.onDraw(canvas)
    }
}