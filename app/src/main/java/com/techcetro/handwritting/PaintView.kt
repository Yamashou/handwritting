package com.techcetro.handwritting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.util.*

class PaintView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint: Paint
    private val path: Path
    private var datas = arrayListOf(arrayListOf<Double>())
    private var allDatas = arrayListOf(arrayListOf(arrayListOf<Double>()))
    private var count = 0.0
    private var countsNum = arrayListOf<Int>()
    init {
        path = Path()
        paint = Paint()
        paint.color = 0xFF000000.toInt()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val data = arrayListOf<Double>(x.toDouble()-500,y.toDouble()-650,count)
        datas.add(data)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                countsNum.add(0)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                countsNum[count.toInt()]++
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                allDatas.add(datas)
                count++
                invalidate()
            }
        }

        Log.d("My app", "X="+ data[0] + " Y=" + data[1] + " Number=" + data[2])
        return true
    }

    fun getCharacter(){
        val co = count.toInt()
        for(i in 1..co+1){
            if(i.equals(co+1)){break}
            val angls = arrayListOf<Double>()
            val setAngls = arrayListOf(arrayListOf<Double>())
            for(j in 1..countsNum[i-1]){
                val x1 = allDatas[i][j][0]
                val x2 = allDatas[i][j+1][0]
                val y1 = allDatas[i][j][1]
                val y2 = allDatas[i][j+1][0]
                if((x1.equals(x2))&&(y1.equals(y2))){continue}
                val xd = getSub(x1,x2)
                val yd = getSub(y1,y2)
                angls.add(getAng(xd,yd))
                if(j+1 == countsNum[i-1]){break}
            }
            for(j in 0..angls.size){
                Log.d("Angl app", "Set1=" + angls[j]+" Set2="+angls[j+1])
                val set = arrayListOf<Double>(angls[j],angls[j+1])
                setAngls.add(set)
                if(j+2 == angls.size){break}
            }
        }
    }
    fun getSub(a:Double,b:Double):Double{
       return Math.max(a,b)-Math.min(a,b)

    }
    fun getAng(a:Double,b:Double):Double{
        return Math.atan2(a,b)
    }

    fun clear() {
        path.reset()
        getCharacter()
        count = 0.0
        datas = arrayListOf(arrayListOf<Double>())
        allDatas = arrayListOf(arrayListOf(arrayListOf<Double>()))
        countsNum = arrayListOf<Int>()
        invalidate()
    }
}
