package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class PaintView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var color: Int = Color.BLACK
    var size: Float = 30F
    var background: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    var text: String = ""
    private val defaultPaint = Paint()

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 100F
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
        canvas.drawText(text,
            (width / 2f) - (textPaint.measureText(text) / 2),
            (height - textPaint.descent() - textPaint.ascent()) / 2f,
            textPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        background?.let {
            val rect = Rect(0, 0, width, height)
            canvas.drawBitmap(it, null, rect, defaultPaint)
        }
    }

    fun generateBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawBackground(canvas)
        drawText(canvas)

        return bitmap
    }
}