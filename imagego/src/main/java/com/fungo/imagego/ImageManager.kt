package com.fungo.imagego

import android.content.Context
import android.view.View
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.glide.transform.RoundType
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.strategy.ImageStrategy
import com.fungo.imagego.utils.ImageConstant
import com.fungo.imagego.utils.ImageUtils
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 10:50 AM
 */
class ImageManager {

    /**
     * 单利对象
     */
    companion object {
        @JvmStatic
        val instance: ImageManager = ImageManager()
    }

    /***
     * 图片的加载策略，抽取常用的图片加载方法到基类里，由具体的去实现，使用时只需要基类接口
     */
    private var mImageStrategy: ImageStrategy? = null


    /**
     * 初始化加载策略，在Application中设置
     * 默认使用Glide加载策略
     * @param strategy　图片加载策略
     */
    fun setImageGoStrategy(strategy: ImageStrategy) {
        mImageStrategy = strategy
    }


    /**
     * 加载网络图片
     * @param any 资源
     * @param view　视图
     */
    fun loadImage(any: Any?,view: View?){
        checkStrategy()
        mImageStrategy!!.loadImage(any,view)
    }

    /**
     * 加载网络图片
     * @param any 资源
     * @param view　视图
     * @param listener　监听
     */
    fun loadImage(any: Any?,view: View?,listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadImage(any,view,listener)
    }


    /**
     * 加载Gif图片
     * @param any　资源
     * @param view　视图
     */
    fun loadGif(any: Any?, view: View?){
        checkStrategy()
        mImageStrategy!!.loadGif(any,view)
    }


    /**
     * 加载Gif
     * @param any　资源
     * @param view　视图
     * @param listener　监听
     */
    fun loadGif(any: Any?, view: View?,listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadGif(any,view,listener)
    }


    /**
     * 加载图片资源，生成Bitmap对象
     * @param context 上下文
     * @param any 图片资源
     * @param listener　加载图片的回调
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadBitmap(context,any,listener)
    }

    /**
     * 加载圆形图片
     * @param url 图片链接
     * @param view　图片
     */
    fun loadCircle(url:String?,view:View?){
        checkStrategy()
        mImageStrategy!!.loadCircle(url,view)
    }


    /**
     * 加载圆形图片
     * @param url 图片链接
     * @param view　展示视图
     * @param borderColor　边框的颜色
     * @param borderWidth　边框的大小
     */
    fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int){
        checkStrategy()
        mImageStrategy!!.loadCircle(url,view,borderWidth,borderColor)
    }


    /**
     * 加载圆形图片
     * @param url 图片链接
     * @param view　展示视图
     * @param borderColor　边框的颜色
     * @param borderWidth　边框的大小
     * @param listener　回调
     */
    fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int,listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadCircle(url,view,borderWidth,borderColor,listener)
    }

    /**
     * 加载圆角图片
     * @param url 资源
     * @param view 视图
     */
    fun loadRound(url: String?,view: View?){
        checkStrategy()
        mImageStrategy!!.loadRound(url,view)
    }

    /**
     * 加载圆角图片
     * @param url 资源
     * @param view 视图
     * @param roundRadius　圆角的角度
     */
    fun loadRound(url: String?,view: View?,roundRadius:Int){
        checkStrategy()
        mImageStrategy!!.loadRound(url,view,roundRadius)
    }


    /**
     * 加载圆角图片
     * @param url 图片链接
     * @param view 展示
     * @param roundRadius　圆角的角度
     * @param roundType　圆角图片的边向
     */
    fun loadRound(url: String?, view: View?, roundRadius:Int, roundType: RoundType){
        checkStrategy()
        mImageStrategy!!.loadRound(url,view,roundRadius,roundType)
    }

    /**
     * 加载圆角图片
     * @param url 图片链接
     * @param view 展示
     * @param roundRadius　圆角的角度
     * @param roundType　圆角图片的边向
     * @param listener 回调
     */
    fun loadRound(url: String?, view: View?, roundRadius:Int, roundType: RoundType, listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadRound(url,view,roundRadius,roundType,listener)
    }


    /**
     * 加载高斯模糊图片
     * @param url　图片链接
     * @param view　展示
     */
    fun loadBlur(url: String?,view: View?){
        checkStrategy()
        mImageStrategy!!.loadBlur(url,view)
    }

    /**
     * 加载高斯模糊图片
     * @param url　图片链接
     * @param view　展示
     * @param blurRadius　高斯模糊的度数
     * @param listener 回调
     */
    fun loadBlur(url: String?,view: View?,blurRadius:Int,listener: OnImageListener?){
        checkStrategy()
        mImageStrategy!!.loadBlur(url,view,blurRadius,listener)
    }


    /**
     * 保存网络图片到本地
     * @param context　上下文
     * @param any　保存的图片资源
     * @param listener　图片保存的回调
     */
    fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?){
        checkStrategy()
        mImageStrategy!!.saveImage(context,any,listener)
    }

    /**
     * 清除图片的磁盘缓存
     * 必须在子线程调用
     * @param context 上下文
     */
    fun clearImageDiskCache(context: Context?){
        checkStrategy()
        mImageStrategy!!.clearImageDiskCache(context)
    }

    /**
     * 清除图片的内存缓存
     * 必须在主线程调用
     * @param context 上下文
     */
    fun clearImageMemoryCache(context: Context?){
        checkStrategy()
        mImageStrategy!!.clearImageMemoryCache(context)
    }

    /**
     * 获取手机磁盘图片缓存大小
     * @param context　上下文
     * @return 缓存大小，格式已经处理好了 例如：100M
     */
    fun getCacheSize(context: Context?): String{
        checkStrategy()
        return mImageStrategy!!.getCacheSize(context)
    }

    /**
     * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
     * @param context　上下文
     */
    fun resumeRequests(context: Context?){
        checkStrategy()
        mImageStrategy!!.resumeRequests(context)
    }

    /**
     * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
     * @param context　上下文
     */
    fun pauseRequests(context: Context?){
        checkStrategy()
        mImageStrategy!!.pauseRequests(context)
    }

    /**
     * 下载网络图片到本地
     * 本方法在子线程执行
     * @param context 上线文
     * @param any　图片的url
     */
    fun download(context: Context, any: Any?): File{
        checkStrategy()
        return mImageStrategy!!.download(context,any)
    }


    /**
     * 加载图片，带进度条
     */
    fun loadProgress(url: String?, view: View?, listener: OnProgressListener) {
        checkStrategy()
        return mImageStrategy!!.loadProgress(url,view,listener)
    }


    /**
     * 检查当前的策略是不是空的，如果是空的则默认使用Glide加载
     */
    private fun checkStrategy() {
        if (mImageStrategy == null) {
            ImageUtils.logD(ImageConstant.LOAD_NULL_STRATEGY)
            mImageStrategy = GlideImageStrategy()
        }
    }

    /**
     * 设置是否打印日志，默认打印日志
     */
    fun setDebug(debug:Boolean){
        ImageConstant.DEBUG = debug
    }

    /**
     * 设置是否自动加载Gif图
     * 如果设置自动加载Gif图，直接调用loadImage()方法即可自动区分普通图片和Gif图片
     */
    fun setAutoGif(autoGif:Boolean){
        ImageConstant.AUTO_GIF = autoGif
    }
}