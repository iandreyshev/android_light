package ru.iandreyshev.player

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

internal class PlayerProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var max: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
            measureSweepAngle()
            invalidate()
        }
    var progress: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
            measureSweepAngle()
            invalidate()
        }
    var progressColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }
    var progressBackgroundColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    private var mSweepAngle = START_ANGLE
    private val mPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBackground()
        canvas.drawProgress()
    }

    private fun Canvas.drawBackground() {
        mPaint.strokeCap = Paint.Cap.SQUARE
        mPaint.color = progressBackgroundColor

        drawCircle(
            width / 2f,
            height / 2f,
            (width - mPaint.strokeWidth) / 2,
            mPaint
        )
    }

    private fun Canvas.drawProgress() {
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.color = progressColor

        drawArc(
            mPaint.strokeWidth / 2,
            mPaint.strokeWidth / 2,
            width.toFloat() - mPaint.strokeWidth / 2,
            height.toFloat() - mPaint.strokeWidth / 2,
            START_ANGLE,
            mSweepAngle,
            false,
            mPaint
        )
    }

    private fun measureSweepAngle() {
        if (max == 0 || progress == 0) {
            mSweepAngle = START_ANGLE
            return
        }

        mSweepAngle = 360f / max * progress
        mSweepAngle = mSweepAngle.coerceAtMost(360f)
    }

    companion object {
        private const val START_ANGLE = -90f
    }

}
