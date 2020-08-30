package com.example.touchview

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

/**
 *Description
 *by cu
 */
class dotInfo(val cx:Float,val cy:Float,val radius:Float,val tag:Int) {
    val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL

    }

    var innerCircleRadius = 0f

    var isSelected = false
    val rect = Rect()

    init {
        rect.top = (cy-radius).toInt()
        rect.bottom = (cy+radius).toInt()
        rect.left = (cx-radius).toInt()
        rect.right = (cx+radius).toInt()

        innerCircleRadius = radius/3.5f
    }

    fun setColor(color: Int){
        paint.color = color
    }

}