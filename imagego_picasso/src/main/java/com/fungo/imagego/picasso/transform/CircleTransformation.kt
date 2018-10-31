package com.fungo.imagego.picasso.transform

import android.graphics.*
import com.squareup.picasso.Transformation


/**
 * @author Pinger
 * @since 18-10-31 下午4:18
 *
 * Picasso 绘制圆形图片，并且可以绘制圆形边框，指定边框颜色
 *
 * @param borderColor 边框颜色
 * @param borderWidth 边框宽度
 *
 */
class CircleTransformation(private val borderWidth: Int = 0, borderColor: Int = 0) : Transformation {

    /**
     * 绘制边框的画笔
     */
    private var mBorderPaint = Paint()

    /**
     * 设置画笔的属性
     */
    init {
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderWidth.toFloat()
    }


    override fun transform(source: Bitmap?): Bitmap? {
        if (source == null) return source

        val width = source.width
        val height = source.height

        val size: Int = Math.min(width, height)

        val x: Int = (width - size) / 2
        val y: Int = (height - size) / 2

        val squaredBitmap: Bitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap: Bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        //绘制圆形
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // 设置shader
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true


        // 将边框和圆形画到canvas上
        val r: Float = size / 2f
        val r1: Float = (size - 2 * borderWidth) / 2f
        canvas.drawCircle(r, r, r1, paint)
        canvas.drawCircle(r, r, r1, mBorderPaint)

        squaredBitmap.recycle()
        return bitmap
    }


    override fun key(): String {
        return "picasso circle"
    }
}