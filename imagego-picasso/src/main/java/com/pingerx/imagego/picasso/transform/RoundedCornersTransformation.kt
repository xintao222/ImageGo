package com.pingerx.imagego.picasso.transform

import android.graphics.*
import com.pingerx.imagego.core.utils.RoundType
import com.squareup.picasso.Transformation


/**
 * @author Pinger
 * @since 18-10-31 下午4:32
 *
 * 图片圆角特效，支持设置多边
 * @param radius
 * @param roundType 圆角类型
 *
 */
class RoundedCornersTransformation(private val radius: Int, private val roundType: RoundType = RoundType.ALL) : Transformation {


    private val diameter: Int = this.radius * 2

    override fun transform(source: Bitmap?): Bitmap? {
        if (source == null) return source

        val width = source.width
        val height = source.height

        // 创建一张可以操作的正方形图片的位图
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // 创建一个画布Canvas
        val canvas = Canvas(bitmap)

        // 创建画笔
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true

        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        source.recycle()
        return bitmap
    }


    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        when (roundType) {
            RoundType.ALL -> canvas.drawRoundRect(RectF(0f, 0f, width, height), radius.toFloat(), radius.toFloat(), paint)
            RoundType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, width, height)
            RoundType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, width, height)
            RoundType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, width, height)
            RoundType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, width, height)
            RoundType.TOP -> drawTopRoundRect(canvas, paint, width, height)
            RoundType.BOTTOM -> drawBottomRoundRect(canvas, paint, width, height)
            RoundType.LEFT -> drawLeftRoundRect(canvas, paint, width, height)
            else -> canvas.drawRoundRect(RectF(0f, 0f, width, height), radius.toFloat(), radius.toFloat(), paint)
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(0f, 0f, diameter.toFloat(), diameter.toFloat()), radius.toFloat(),
                radius.toFloat(), paint)
        canvas.drawRect(RectF(0f, radius.toFloat(), radius.toFloat(), bottom), paint)
        canvas.drawRect(RectF(radius.toFloat(), 0f, right, bottom), paint)
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - diameter, 0f, right, diameter.toFloat()), radius.toFloat(),
                radius.toFloat(), paint)
        canvas.drawRect(RectF(0f, 0f, right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, radius.toFloat(), right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(0f, bottom - diameter, diameter.toFloat(), bottom), radius.toFloat(),
                radius.toFloat(), paint)
        canvas.drawRect(RectF(0f, 0f, diameter.toFloat(), bottom - radius), paint)
        canvas.drawRect(RectF(radius.toFloat(), 0f, right, bottom), paint)
    }

    private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
                radius.toFloat(), paint)
        canvas.drawRect(RectF(0f, 0f, right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, 0f, right, bottom - radius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(0f, 0f, right, diameter.toFloat()), radius.toFloat(), radius.toFloat(),
                paint)
        canvas.drawRect(RectF(0f, radius.toFloat(), right, bottom), paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(0f, bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
                paint)
        canvas.drawRect(RectF(0f, 0f, right, bottom - radius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(0f, 0f, diameter.toFloat(), bottom), radius.toFloat(), radius.toFloat(),
                paint)
        canvas.drawRect(RectF(radius.toFloat(), 0f, right, bottom), paint)
    }

    override fun key(): String {
        return "picasso round"
    }

}