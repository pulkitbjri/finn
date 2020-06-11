package com.example.eezytask.slotsCustomView

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import com.example.finn.R


class CustomGrooveView : View{
    val TAG = CustomGrooveView::class.java.simpleName
    private var mRect: RectF=RectF()
    private var hideBottom: Boolean = false
    private var hideTop: Boolean = false
    private val mPath = Path()
    private val mBackgroundPaint = Paint()
    private val grooveDepth:Int= com.example.finn.customcard.Utils.dpToPx(14f,context!!)
    private val grooveHeight :Int= com.example.finn.customcard.Utils.dpToPx(10f,context!!)
    private val grooveHeightCenter :Int= com.example.finn.customcard.Utils.dpToPx(4f,context!!)
    private val grooveDepthCenter :Int= com.example.finn.customcard.Utils.dpToPx(4f,context!!)

    private var mShadow: Bitmap? = null
    private val mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mShadowRadius: Int= com.example.finn.customcard.Utils.dpToPx(4f,context!!)

    constructor(context: Context) : super(context){
        init(null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    {
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs!=null){

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomGrooveView)

            hideTop = typedArray.getBoolean(R.styleable.CustomGrooveView_hideTop, false)
            hideBottom = typedArray.getBoolean(R.styleable.CustomGrooveView_hideBottom, false)


            typedArray.recycle()
        }

        setBackgroundPaint()
        mShadowPaint.colorFilter = PorterDuffColorFilter(
            Color.BLACK,
            PorterDuff.Mode.SRC_IN
        )
        mShadowPaint.alpha = 51
        invalidate()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        doLayout()
        if ( !isInEditMode && mShadow!=null) {
            canvas!!.drawBitmap(mShadow!!, 0f, mShadowRadius / 2f, null)
        }
        canvas?.drawPath(mPath,mBackgroundPaint)

    }

    private fun doLayout() {
        mPath.reset()
        val left: Float = paddingLeft.toFloat() + mShadowRadius
        val right: Float = (width ).toFloat() - mShadowRadius
        val top: Float = if (!hideTop) paddingTop.toFloat()
                        else paddingTop.toFloat()+ mShadowRadius
        val bottom: Float = if (!hideBottom) (height).toFloat()
                            else  (height).toFloat() -mShadowRadius -mShadowRadius/2


        if (!hideTop) {
            mPath.moveTo(left, top + grooveHeight)
            mPath.lineTo(left + grooveDepth - grooveDepthCenter, top + grooveHeightCenter)
            mPath.lineTo(left + grooveDepth, top)

            mPath.lineTo(right - grooveDepth, top)
            mPath.lineTo(right - grooveDepth + grooveHeightCenter, top + grooveHeightCenter)
            mPath.lineTo(right, top + grooveHeight)
        }else{
            mPath.moveTo(left, top+grooveHeight )
//
            mRect= RectF(left,top,left+grooveHeight*2,top+grooveHeight*2)
            mPath.arcTo(mRect,180f,90f)

            mPath.lineTo(right-grooveHeight*2,top)

            mRect= RectF(right-grooveHeight*2,top,right,top+grooveHeight*2)
            mPath.arcTo(mRect,270f,90f)

//            mPath.lineTo(right, top)
        }
        if (!hideBottom) {
            mPath.lineTo(right, bottom - grooveHeight)
            mPath.lineTo(right - grooveDepth + grooveDepthCenter, bottom - grooveHeightCenter)
            mPath.lineTo(right - grooveDepth, bottom)

            mPath.lineTo(left + grooveDepth, bottom)
            mPath.lineTo(left + grooveDepth - grooveDepthCenter, bottom - grooveHeightCenter)
            mPath.lineTo(left, bottom - grooveHeight)

            mPath.lineTo(left,top+grooveHeight)
        }
        else
        {
            mPath.lineTo(right, bottom-grooveHeight )

            mRect= RectF(right-grooveHeight*2,bottom-grooveHeight*2,right,bottom)
            mPath.arcTo(mRect,0f,90f)

            mPath.lineTo(left+grooveHeight , bottom)

            mRect= RectF(left,bottom-grooveHeight*2,left+grooveHeight*2,bottom)
            mPath.arcTo(mRect,90f,90f)

            mPath.lineTo(left, top + grooveHeight)
        }

        mPath.close()
        generateShadow()
    }


    private fun generateShadow() {
        if (!isInEditMode) {
            if (mShadow==null) {
                mShadow = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
            } else {
                mShadow?.eraseColor(Color.TRANSPARENT)
            }
            val c = Canvas(mShadow!!)
            c.drawPath(mPath, mShadowPaint)

            val rs = RenderScript.create(context)
            val blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs))
            val input = Allocation.createFromBitmap(rs, mShadow)
            val output = Allocation.createTyped(rs, input.type)
            blur.setRadius(mShadowRadius.toFloat())
            blur.setInput(input)
            blur.forEach(output)
            output.copyTo(mShadow)
            input.destroy()
            output.destroy()
            blur.destroy()
        }
    }

    private fun setBackgroundPaint() {
        mBackgroundPaint.alpha = 0
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = resources.getColor(android.R.color.white)
        mBackgroundPaint.style = Paint.Style.FILL
    }
}