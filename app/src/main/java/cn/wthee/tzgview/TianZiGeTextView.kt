package cn.wthee.tzgview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup


class TianZiGeTextView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    //画笔
    private var mPaint = Paint()
    private var mPathPaint = Paint()
    private var mTextPaint = Paint()

    //线
    private var mRects = arrayListOf<Rect>()
    private var mDottedLines = arrayListOf<Path>()

    //自定义
    private var mLineColor = Color.RED
    private var mLineWidth = 5f
    private var mWordSpace = 10
    private var mSize = 150f
    private var mWord = ""
    private var mWordSize = 80f
    private var mWordColor = Color.BLACK
    //文本居中
    private var distance = 0f

    //初始化
    private fun init() {
        //边框画笔
        mPaint.color = mLineColor
        mPaint.strokeWidth = mLineWidth
        mPaint.style = Paint.Style.STROKE
        //虚线画笔
        mPathPaint.color = mLineColor
        mPathPaint.strokeWidth = mLineWidth / 2
        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.pathEffect = DashPathEffect(floatArrayOf(8f, 8f), 0f)
        //文字画笔
        mTextPaint.color = mWordColor
        mTextPaint.textSize = mWordSize
        mTextPaint.textAlign = Paint.Align.CENTER
        val fontMetrics: Paint.FontMetrics = mTextPaint.fontMetrics
        distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

        //边框和虚线
        mRects.clear()
        mDottedLines.clear()
        for (i in mWord.indices) {
            //边框
            var left =
                if (i > 0) i * (mSize.toInt() + mWordSpace) else i * mSize.toInt()
            //设置居中
            if (this.gravity == Gravity.CENTER) {
                val textLength = mWord.length
                val parent = parent as ViewGroup
                val width = parent.width
                //父布局中心为参考的偏移量
                val offset = (i - textLength / 2f) * mSize + (i - (textLength - 1) / 2f) * mWordSpace
                left = (width / 2f + offset).toInt()
            }
            val top = 0
            val right = left + mSize.toInt()
            val bottom = mSize.toInt()

            val rect = Rect(left, top, right, bottom)
            mRects.add(rect)
            //虚线
            for (j in 1..4) {
                val path = Path()
                when (j) {
                    1 -> {
                        //左上 -> 右下
                        path.moveTo(left.toFloat(), 0f)
                        path.lineTo(right.toFloat(), bottom.toFloat())
                    }
                    2 -> {
                        //左下 -> 右上
                        path.moveTo(left.toFloat(), bottom.toFloat())
                        path.lineTo(right.toFloat(), 0f)
                    }
                    3 -> {
                        //竖线
                        path.moveTo(left + mSize / 2, 0f)
                        path.lineTo(left + mSize / 2, mSize)
                    }
                    4 -> {
                        //横线
                        path.moveTo(left.toFloat(), mSize / 2)
                        path.lineTo(right.toFloat(), mSize / 2)
                    }
                }
                path.close()
                mDottedLines.add(path)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //加载画笔
        init()
        //绘制边框及文本
        mRects.forEachIndexed { index, rect ->
            val left = rect.left
            canvas?.drawRect(rect, mPaint)
            val pathIndex = index * 4
            canvas?.drawPath(mDottedLines[pathIndex], mPathPaint)
            canvas?.drawPath(mDottedLines[pathIndex + 1], mPathPaint)
            canvas?.drawPath(mDottedLines[pathIndex + 2], mPathPaint)
            canvas?.drawPath(mDottedLines[pathIndex + 3], mPathPaint)
            //计算文字基线
            val baseline = rect.centerY() + distance
            mTextPaint.getTextBounds(mWord[index].toString(), 0, 1, rect)
            canvas!!.drawText(
                mWord[index].toString(),
                left + mSize / 2,
                baseline,
                mTextPaint
            )
        }
    }

    //设置文本
    fun setWord(text: String?) {
        if (text != null) {
            this.mWord = text.replace("\r\n|\r|\n".toRegex(), "").replace(" ","")
        }
    }
    //设置文本大小
    fun setWordSize(size: Float) {
        this.mWordSize = size
    }
    //设置文本大小
    fun setWordColor(color: Int) {
        this.mWordColor = color
    }
    //设置边框及虚线颜色
    fun setLineColor(color: Int) {
        this.mLineColor = color
    }
    //设置边框及虚线粗细
    fun setLineWidth(lineWidth: Float) {
        this.mLineWidth = lineWidth
    }
    //设置边框大小
    fun setLineSize(size: Float) {
        this.mSize = size
        reSizeHeight(size)
    }
    //设置文本间隔
    fun setWordSpace(textSpace: Int) {
        this.mWordSpace = textSpace
    }

    fun updateView(){
        postInvalidate()
    }

    private fun reSizeHeight(height: Float){
        val params = this.layoutParams
        params.height = height.toInt()
        this.layoutParams = params
    }
}