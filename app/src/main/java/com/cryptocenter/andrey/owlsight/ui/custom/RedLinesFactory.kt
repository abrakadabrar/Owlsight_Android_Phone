package com.cryptocenter.andrey.owlsight.ui.custom

import java.util.*

/**
 * @author repitch
 */
class RedLinesFactory {

    fun createRedLines(redSeconds: List<Int>): List<RedLine> {
        if (redSeconds.isEmpty()) {
            return emptyList()
        }
        val redLines = ArrayList<RedLine>()
        var startSecond = redSeconds[0]
        var curSecond = startSecond
        for (i in 1 until redSeconds.size) {
            val prevSecond = redSeconds[i - 1]
            curSecond = redSeconds[i]
            if (curSecond - prevSecond == 1) {
                continue
            }
            redLines.add(RedLine(startSecond, prevSecond))
            startSecond = curSecond
        }
        redLines.add(RedLine(startSecond, curSecond))
        return redLines
    }

    fun updateWithSize(
        redLines: List<RedLine>,
        minSec: Float, maxSec: Float,
        left: Float, right: Float
    ): List<RedLine> {
        val width = right - left
        val totalSec = maxSec - minSec
        val secWidth = width / totalSec
        val b = left - secWidth * minSec
        // x = k * sec + b
        // min sec -> left : left = k * minSec + b
        // max sec -> right: right = k * maxSec + b
        // right - left = k * (maxSec - minSec)
        // k = width / totalSec = secWidth
        // b = left - secWidth * minSec
        // x = secWidth * sec + b
        for (redLine in redLines) {
            val startSec = redLine.startSec
            redLine.startXPos = secWidth * startSec + b
            val endSec = redLine.endSec
            redLine.endXPos = secWidth * (endSec + 1) + b
        }
        return redLines
    }
}
