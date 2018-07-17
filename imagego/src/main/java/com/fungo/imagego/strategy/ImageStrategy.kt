package com.fungo.imagego.strategy


import android.content.Context
import android.view.View
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 */

interface ImageStrategy {

    /**
     * 加载网络图片，支持png，jpeg,jpg等格式
     * @param url 图片的网络链接
     * @param view 图片展示
     */
    fun loadImage(url: String?, view: View?)

    /**
     * 加载网络图片，支持png，jpeg,jpg等格式
     * @param url 图片的网络链接
     * @param view 图片展示
     * @param listener 图片加载监听
     */
    fun loadImage(url: String?, view: View?, listener: OnImageListener?)

    /**
     * 通过其他资源加载图片
     * @param obj:图片资源，可以是File,Bitmap,URI,ResID,Drawable
     * @param view:图片展示
     */
    fun loadImage(obj: Any?, view: View?)



    /**
     * 加载Gif网络图片
     * @param url Gif图片的网络链接
     * @param view　展示的View
     */
    fun loadGif(url: String?, view: View?)


    /**
     * 加载Gif网络图片
     * @param url Gif图片的网络链接
     * @param view　展示的View
     * @param listener　加载监听
     */
    fun loadGif(url: String?, view: View?, listener: OnImageListener?)


    /**
     * 加载图片资源，生成Bitmap对象
     * @param context 上下文
     * @param any 图片资源
     * @param listener　加载图片的回调
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener?)


    /**
     * 保存网络图片到本地
     * @param context　上下文
     * @param any　保存的图片资源
     * @param listener　图片保存的回调
     */
    fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?)

    /**
     * 清除图片的磁盘缓存
     * 必须在子线程调用
     * @param context 上下文
     */
    fun clearImageDiskCache(context: Context?)

    /**
     * 清除图片的内存缓存
     * 必须在主线程调用
     * @param context 上下文
     */
    fun clearImageMemoryCache(context: Context?)

    /**
     * 获取手机磁盘图片缓存大小
     * @param context　上下文
     * @return 缓存大小，格式已经处理好了 例如：100M
     */
    fun getCacheSize(context: Context?): String

    /**
     * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
     * @param context　上下文
     */
    fun resumeRequests(context: Context?)

    /**
     * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
     * @param context　上下文
     */
    fun pauseRequests(context: Context?)

    /**
     * 下载网络图片到本地
     * 本方法在子线程执行
     * @param context 上线文
     * @param any　图片的url
     */
    fun download(context: Context, any: Any?): File
}
