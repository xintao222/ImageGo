@file:JvmName("ImageGo")

package com.fungo.imagego

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.strategy.ImageGoEngine
import com.fungo.imagego.strategy.ImageOptions
import com.fungo.imagego.strategy.ImageStrategy
import com.fungo.imagego.utils.RoundType
import java.io.File


// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－


//============================================================================
fun loadImage(any: Any?, view: View?) {
    loadImage(any, view, null)
}


fun loadImage(any: Any?, view: View?, listener: OnImageListener?) {
    loadImage(any, view, -1, listener)
}


fun loadImage(any: Any?, view: View?, placeHolder: Int) {
    loadImage(any, view, placeHolder, null)
}

fun loadImage(any: Any?, view: View?, placeholder: Int, listener: OnImageListener?) {
    loadImage(any, view, listener, getDefaultBuilder().setPlaceHolderResId(placeholder).build())
}


fun loadImage(any: Any?, view: View?, options: ImageOptions) {
    loadImage(any, view, null, options)
}


/**
 * 加载网络图片，可以配置加载监听，和其他Options配置项
 * @param any 图片资源
 * @param view 展示的View
 * @param listener 监听加载对象
 * @param options 图片加载配置项
 */
fun loadImage(any: Any?, view: View?, listener: OnImageListener?, options: ImageOptions) {
    getStrategy().loadImage(any, view, listener, options)
}


//============================================================================
/**
 * 加载图片，带进度条
 * @param any 图片资源
 * @param view 展示的View
 * @param listener 加载监听
 */
fun loadProgress(any: Any?, view: View?, listener: OnProgressListener) {
    getStrategy().loadProgress(any, view, listener)
}


//============================================================================
/**
 * 异步加载图片资源，生成Bitmap对象
 * 可以在主线程直接调用
 * @param context 上下文
 * @param any 图片资源
 * @param listener　加载图片的回调
 */
fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener) {
    getStrategy().loadBitmap(context, any, listener)
}


/**
 * 同步加载图片资源，生成Bitmap对象
 * 必须在子线程调用，并且处理异常
 * @param context 上下文
 * @param any 图片资源
 * @return Bitmap对象，可能为null
 */
fun loadBitmap(context: Context?, any: Any?): Bitmap? {
    return getStrategy().loadBitmap(context, any)
}


//============================================================================
fun loadCircle(any: Any?, view: View?) {
    loadCircle(any, view, -1, -1)
}

fun loadCircle(any: Any?, view: View?, borderWidth: Int, borderColor: Int) {
    loadCircle(any, view, borderWidth, borderColor, null)
}


/**
 * 加载圆形图片
 * @param any 图片资源
 * @param view　展示视图
 * @param borderWidth　边框的大小
 * @param borderColor　边框的颜色
 * @param listener　加载回调
 */
fun loadCircle(any: Any?, view: View?, borderWidth: Int, borderColor: Int, listener: OnImageListener?) {
    loadImage(any, view, listener, getDefaultBuilder()
            .setCrossFade(false)
            .setCircle(true)
            .setCircleBorderColor(borderColor)
            .setCircleBorderWidth(borderWidth).build())
}


//============================================================================
fun loadRound(any: Any?, view: View?) {
    loadRound(any, view, -1)
}


fun loadRound(any: Any?, view: View?, roundRadius: Int) {
    loadRound(any, view, roundRadius, RoundType.ALL)
}

fun loadRound(any: Any?, view: View?, roundRadius: Int, roundType: RoundType) {
    loadRound(any, view, roundRadius, roundType, null)
}

/**
 * 加载圆角图片
 * @param any 图片链接
 * @param view 展示
 * @param roundRadius　圆角的角度
 * @param roundType　圆角图片的边向
 * @param listener 回调
 */
fun loadRound(any: Any?, view: View?, roundRadius: Int, roundType: RoundType, listener: OnImageListener?) {
    loadImage(any, view, listener, getDefaultBuilder()
            .setCrossFade(false)
            .setRoundedCorners(true)
            .setRoundRadius(roundRadius)
            .setRoundType(roundType).build())
}


//============================================================================

fun loadBlur(any: Any?, view: View?) {
    loadBlur(any, view, -1)
}

fun loadBlur(any: Any?, view: View?, blurRadius: Int) {
    loadBlur(any, view, blurRadius, null)
}

/**
 * 加载高斯模糊图片
 * @param any　图片链接
 * @param view　展示
 * @param blurRadius　高斯模糊的度数
 * @param listener 回调
 */
fun loadBlur(any: Any?, view: View?, blurRadius: Int, listener: OnImageListener?) {
    loadImage(any, view, listener, getDefaultBuilder()
            .setBlur(true)
            .setBlurRadius(blurRadius).build())
}


//============================================================================
fun saveImage(context: Context?, any: Any?) {
    saveImage(context, any, null)
}

/**
 * 保存网络图片到本地
 * @param context　上下文
 * @param any　保存的图片资源
 * @param listener　图片保存的回调
 */
fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?) {
    getStrategy().saveImage(context, any, listener)
}

//============================================================================

/**
 * 清除图片的磁盘缓存
 * 必须在子线程调用
 * @param context 上下文
 */
fun clearImageDiskCache(context: Context?) {
    getStrategy().clearImageDiskCache(context)
}

/**
 * 清除图片的内存缓存
 * 必须在主线程调用
 * @param context 上下文
 */
fun clearImageMemoryCache(context: Context?) {
    getStrategy().clearImageMemoryCache(context)
}

/**
 * 获取手机磁盘图片缓存大小
 * @param context　上下文
 * @return 缓存大小，格式已经处理好了 例如：100M
 */
fun getCacheSize(context: Context?): String {
    return getStrategy().getCacheSize(context)
}

//============================================================================

/**
 * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
 * @param context　上下文
 */
fun resumeRequests(context: Context?) {
    getStrategy().resumeRequests(context)
}

/**
 * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
 * @param context　上下文
 */
fun pauseRequests(context: Context?) {
    getStrategy().pauseRequests(context)
}


//============================================================================

/**
 * 下载网络图片到本地
 * 本方法在子线程执行
 * @param context 上线文
 * @param any　图片资源
 */
fun downloadImage(context: Context, any: Any?): File {
    return getStrategy().downloadImage(context, any)
}


//============================================================================
//============================================================================

/**
 * 获取默认的配置,可以手动配置
 */
private fun getDefaultBuilder(): ImageOptions.Builder {
    return getStrategy().getDefaultBuilder()
}


/**
 * 获取图片加载策略
 */
private fun getStrategy(): ImageStrategy {
    return ImageGoEngine.getStrategy()
}





