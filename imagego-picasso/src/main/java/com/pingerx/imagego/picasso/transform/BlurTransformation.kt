package com.pingerx.imagego.picasso.transform

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.RequiresApi
import com.squareup.picasso.Transformation


/**
 * @author Pinger
 * @since 18-10-31 下午4:12
 *
 * Picasso高斯模糊特效
 * @param radius 高斯的程度
 */
class BlurTransformation(context: Context?, private val radius: Int = 25, private val blurSampling: Int) : Transformation {

    private val renderScript: RenderScript = RenderScript.create(context)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun transform(source: Bitmap?): Bitmap? {
        if (source == null) return source

        // 创建一个Bitmap作为最后处理的效果Bitmap
        val blurredBitmap: Bitmap? = source.copy(Bitmap.Config.ARGB_8888, true)

        // 分配内存
        val input: Allocation = Allocation.createFromBitmap(renderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED)
        val output: Allocation = Allocation.createTyped(renderScript, input.type)

        // 根据我们想使用的配置加载一个实例
        val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        script.setInput(input)

        // 设置模糊半径
        script.setRadius(radius * blurSampling * 1.0f)

        //开始操作
        script.forEach(output)

        // 将结果copy到blurredBitmap中
        output.copyTo(blurredBitmap)

        //释放资源
        source.recycle()

        return blurredBitmap
    }

    override fun key(): String {
        return "picasso blur"
    }
}