package com.cryptocenter.andrey.owlsight.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author repitch
 */
class VideoControlsTimelineView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attr, defStyleAttr) {

    private val paintTimeline = Paint().apply {
        color = Color.RED
    }
    private val redLinesFactory = RedLinesFactory()
    private var redLines: List<RedLine> = emptyList()
    private var currentInterval: Pair<Int, Int>? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        currentInterval?.takeIf { redLines.isNotEmpty() }?.let { interval ->
            redLinesFactory.updateWithSize(
                redLines,
                interval.first.toFloat(), interval.second.toFloat(),
                0f, width.toFloat()
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        redLines.forEach{redLine ->
            canvas.drawRect(
                redLine.startXPos, 0f,
                redLine.endXPos, height.toFloat(),
                paintTimeline
            )
        }
        canvas.restore()
    }

    fun setIntervalData(seconds: List<Int>, interval: Pair<Int, Int>) {
        redLines = redLinesFactory.createRedLines(seconds)
        currentInterval = interval
        invalidate()
    }

}
