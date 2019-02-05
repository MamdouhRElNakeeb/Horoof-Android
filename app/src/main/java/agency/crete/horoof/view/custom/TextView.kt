package agency.crete.horoof.view.custom

import agency.crete.horoof.R
import agency.crete.horoof.R.attr.strokeColor
import agency.crete.horoof.R.attr.strokeWidth
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue

class TextView : AppCompatTextView {

    private val defaultOutlineWidth = 0F
    private var isDrawing: Boolean = false

    private var outlineColor: Int = 0
    private var outlineWidth: Float = 0.toFloat()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
            outlineColor = a.getColor(R.styleable.OutlineTextView_outlineColor, currentTextColor)
            outlineWidth = a.getDimension(R.styleable.OutlineTextView_outlineWidth, defaultOutlineWidth)

            a.recycle()
        } else {
            outlineColor = currentTextColor
            outlineWidth = defaultOutlineWidth
        }
        setOutlineWidth(TypedValue.COMPLEX_UNIT_PX, outlineWidth)
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
    }


    fun setOutlineWidth(unit: Int, width: Float) {
        outlineWidth = TypedValue.applyDimension(
            unit, width, context.resources.displayMetrics)
    }

    override fun invalidate() {
        // prevent onDraw.setTextColor force redraw
        if (isDrawing) return
        super.invalidate()
    }

    override fun onDraw(canvas: Canvas) {

//        if(strokeColor!=null)
//        {
//            val textColor = textColors
//
//            val paint = this.paint
//
//            paint.style = Paint.Style.STROKE
//            paint.strokeJoin = strokeJoin
//            paint.strokeMiter = strokeMiter    //10f
//            this.setTextColor(strokeColor)
//            paint.strokeWidth = strokeWidth    //15f
//
//            super.onDraw(canvas)
//            paint.style = Paint.Style.FILL
//
//            setTextColor(textColor)
//            super.onDraw(canvas)
//
//        }
         if (outlineWidth > 0) {
             isDrawing = true

             val currentTextColor = currentTextColor
             paint.style = Paint.Style.STROKE
             paint.strokeWidth = outlineWidth
             setTextColor(outlineColor)
             super.onDraw(canvas)

             paint.style = Paint.Style.FILL
             setTextColor(currentTextColor)
             super.onDraw(canvas)
             isDrawing = false

        }
        else {
            super.onDraw(canvas)
        }

    }
}