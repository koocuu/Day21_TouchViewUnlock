package com.example.touchview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.StringBuilder

/**
 *Description
 *by cu
 */
class TouchUnlockView :View{
    private var radius = 0f
    private var padding = 0f
    private val dotPath = Path()
    private val dotPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val dotInfos = mutableListOf<dotInfo>()
    private val selectedItems = mutableListOf<dotInfo>()
    //保存上一个点亮的点
    private var lastSelectedItem :dotInfo? = null
    private var endPoint = Point()
    private val linePath = Path()
    private val linePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val innerCircle = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val password = StringBuilder()
    constructor(context: Context):super (context){}

    constructor(context: Context,attrs:AttributeSet):super(context,attrs){}

    constructor(context: Context,attrs: AttributeSet,style:Int):super(context,attrs,style){}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    override fun onDraw(canvas: Canvas?) {

        //划线
        canvas?.drawPath(linePath,linePaint)

        if (!endPoint.equals(0,0)){
            canvas?.drawLine(lastSelectedItem!!.cx,lastSelectedItem!!.cy,
                            endPoint.x.toFloat(),endPoint.y.toFloat(),linePaint
                )
        }
        //画点
        drawDots(canvas)

    }

    private fun init(){
        //第一个点的坐标
        var cx = 0f
        var cy = 0f
        if(measuredWidth >= measuredHeight){
            radius = measuredHeight/5/2f
            padding = (measuredHeight-6*radius)/4
            cx = (measuredWidth-measuredHeight)+padding+radius
            cy = padding+radius
        }else{
            radius = measuredWidth/5/2f
            padding = (measuredWidth-6*radius)/4
            cx = (measuredHeight-measuredWidth)+padding+radius
            cy = padding+radius
        }

        for (row in 0..2){
            for (column in 0..2){
                dotInfo(cx+column*(2*radius+padding),
                    cy+row*(2*radius+padding),
                    radius,row*3 + column +1).also {
                    dotInfos.add(it)
                }
            }
        }



    }

    fun drawDots(canvas: Canvas?){
        for (info in dotInfos){
            canvas?.drawCircle(info.cx,info.cy,info.radius,info.paint)
            canvas?.drawCircle(info.cx,info.cy,info.radius-2,innerCircle)
            if (info.isSelected){
                canvas?.drawCircle(info.cx,info.cy,info.innerCircleRadius,info.paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                containsPoint(x, y).also {

                    if (it != null) {
                        selectItem(it)
                        linePath.moveTo(it.cx,it.cy)
                    }

                }
            }
            MotionEvent.ACTION_MOVE -> {
                containsPoint(x,y).also {
                    if (it != null){
                        if(!it.isSelected){
                            if (lastSelectedItem == null){
                                linePath.moveTo(it.cx,it.cy)
                            }else{
                                linePath.lineTo(it.cx,it.cy)
                            }
                            selectItem(it)
                        }
                    }else{
                        //触摸点在外部
                        if (lastSelectedItem != null){
                            endPoint.set(x!!.toInt(),y!!.toInt())
                            invalidate()
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                reset()
            }



        }
        return true
    }

    //重设点
    private fun reset(){
        for (item in selectedItems){
            item.isSelected = false
        }
        linePath.reset()
        invalidate()
        selectedItems.clear()
        password.clear()
    }

    //点亮点
    private fun selectItem(item: dotInfo){
        item.isSelected = true
        invalidate()
        selectedItems.add(item)
        lastSelectedItem = item
        endPoint.set(0,0)
        password.append(item)

    }

    private fun containsPoint(x:Float?,y:Float?): dotInfo? {
        for (item in dotInfos){
            if (item.rect.contains(x!!.toInt(),y!!.toInt())){
                return item
            }
        }
        return null
    }
}
