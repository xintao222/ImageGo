package com.fungo.imagego.glide

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.strategy.ImageConfig
import com.fungo.imagego.strategy.ImageStrategy
import com.fungo.imagego.utils.ImageConstant
import com.fungo.imagego.utils.ImageUtils
import java.io.File


/**
 * @author Pinger
 * @since 3/28/18 2:16 PM
 *
 * 使用Glide加载图片策略
 * 更多Glide使用请看官方使用手册[https://muyangmin.github.io/glide-docs-cn/doc/caching.html]
 *
 */
class GlideImageStrategy : ImageStrategy {

    /**
     * 默认的配置,可以手动配置
     */
    private val builder = ImageConfig
            .Builder()
            .setPlaceHolderDrawable(ColorDrawable(Color.parseColor(ImageConstant.IMAGE_PLACE_HOLDER_COLOR)))
            .setDiskCacheStrategy(ImageConfig.DiskCache.AUTOMATIC)
            .setPriority(ImageConfig.LoadPriority.NORMAL)
            .setCrossFade(true)
            .setAsGif(false)

    override fun loadImage(url: String?, view: View?) {
        loadImage(url, view, null)
    }

    override fun loadImage(url: String?, view: View?, listener: OnImageListener?) {
        loadImage(url, view, builder.build(), listener)
    }

    override fun loadImage(obj: Any?, view: View?) {
        loadImage(obj, view, builder.build(), null)
    }

    override fun loadGif(url: String?, view: View?) {
        loadGif(url, view, null)
    }

    override fun loadGif(url: String?, view: View?, listener: OnImageListener?) {
        loadImage(url, view, builder.setAsGif(true).build(), listener)
    }


    /**
     * 加载bitmap
     * 在主线程调用
     */
    override fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener?) {
        if (context == null || any == null) {
            listener?.onFail(ImageConstant.ERROR_LOAD_NULL_CONTEXT_ANY)
            ImageUtils.logD(ImageConstant.ERROR_LOAD_NULL_CONTEXT_ANY)
            return
        }
        // submit方法要在子线程调用
        ImageUtils.runOnSubThread(Runnable {
            val bitmap = Glide.with(context).asBitmap().load(any).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            ImageUtils.runOnUIThread(Runnable {
                // 加载成功在主线程回调
                listener?.onSuccess(bitmap)
            })
        })
    }

    /**
     * 保存图片到本地
     * 可以在主线程调用
     */
    override fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?) {
        if (context == null || any == null) {
            listener?.onSaveFail(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            ImageUtils.logD(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            return
        }
        ImageUtils.runOnSubThread(Runnable {
            try {
                // 图片后缀
                val suffix = if (ImageUtils.isGif(any)) {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_GIF
                } else {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_JPG
                }

                // 保存的位置
                val destFile = File(ImageUtils.getImageSavePath(context) + suffix)
                // 要保存的原图
                val imageFile = download(context, any)
                // 进行保存
                val isCopySuccess = ImageUtils.copyFile(imageFile, destFile)

                // 最后通知图库更新
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(destFile)))
                // 主线程回调
                ImageUtils.runOnUIThread(Runnable {
                    if (isCopySuccess) {
                        listener?.onSaveSuccess(ImageConstant.SAVE_PATH + ImageUtils.getImageSavePath(context))
                    } else {
                        listener?.onSaveFail(ImageConstant.SAVE_FAIL)
                    }
                })
            } catch (e: Exception) {
                ImageUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail(ImageConstant.SAVE_FAIL)
                    ImageUtils.logE(ImageConstant.SAVE_FAIL + ": " + e.message)
                })
            }
        })
    }


    /**
     * 清理磁盘缓存
     * 可以主线程调用
     */
    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearDiskCache()
        }else{
            ImageUtils.logD(ImageConstant.CLEAR_NULL_CONTEXT)
        }
    }

    /**
     * 清除内存缓存
     * 只能在主线程调用
     */
    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        }else{
            ImageUtils.logD(ImageConstant.CLEAR_NULL_CONTEXT)
        }
    }


    /**
     * 获取本地缓存大小
     * 同步方法
     */
    override fun getCacheSize(context: Context?): String {
        return ImageUtils.getImageCacheSize(context)
    }

    /**
     * 重新加载
     */
    override fun resumeRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).resumeRequests()
        }
    }


    /**
     * 暂停加载
     */
    override fun pauseRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).pauseRequests()
        }
    }


    /**
     * 缓存图片文件
     */
    override fun download(context: Context, any: Any?): File {
        return Glide.with(context).download(any).submit().get()
    }

    /**
     * Glide加载图片的主要方法
     * @param obj 图片资源
     * @param view 图片展示控件
     * @param config 图片配置
     * @param listener 图片加载回调
     */
    private fun loadImage(obj: Any?, view: View?, config: ImageConfig, listener: OnImageListener?) {
        if (obj == null||view == null) {
            listener?.onFail("GlideImageStrategy：image request url is null...")
            return
        }

        val context = view.context
        if (context == null) {
            listener?.onFail("GlideImageStrategy：context is null...")
            return
        }

        if (obj is String) {
            if (TextUtils.isEmpty(obj)) {
                listener?.onFail("GlideImageStrategy：image request url is null...")
                return
            }
        }

        try {
            if (config.asGif) {
                val gifBuilder = Glide.with(context).asGif().load(obj)
                val builder = buildGift(context, obj, config, gifBuilder, listener)
                // 使用clone方法复用builder，有缓存不会请求网络
                if (view is ImageView) {
                    builder.clone().apply(buildOptions(context, obj, config)).into(view)
                } else throw IllegalStateException("Glide只支持ImageView展示图片")
            } else {
                val bitmapBuilder = Glide.with(context).asBitmap().load(obj)
                val builder = buildBitmap(context, obj, config, bitmapBuilder, listener)
                if (view is ImageView) {
                    builder.clone().apply(buildOptions(context, obj, config)).into(view)
                } else throw IllegalStateException("Glide只支持ImageView展示图片")
            }
        } catch (e: Exception) {
            listener?.onFail("GlideImageStrategy：load image exception: " + e.message)
            if (view is ImageView) {
                view.setImageResource(config.errorResId)
            }
        }
    }


    /**
     * 设置bitmap属性
     */
    private fun buildBitmap(context: Context, obj: Any, config: ImageConfig, bitmapBuilder: RequestBuilder<Bitmap>, listener: OnImageListener?): RequestBuilder<Bitmap> {
        var builder = bitmapBuilder
        // 渐变展示
        if (config.isCrossFade) {
            builder.transition(BitmapTransitionOptions.withCrossFade())
        }

        // 缩略图大小
        if (config.thumbnail > 0f) {
            builder.thumbnail(config.thumbnail)
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(config.thumbnailUrl)) {
            val thumbnailBuilder = Glide.with(context).asBitmap().load(obj).thumbnail(Glide.with(context).asBitmap().load(config.thumbnailUrl))
            builder = thumbnailBuilder
        }

        // 加载监听
        builder.listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message ?: "GlideImageStrategy：image load fail")
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource)
                return false
            }
        })
        return builder
    }


    /**
     * 设置Gift属性
     */
    private fun buildGift(context: Context, obj: Any, config: ImageConfig, gifBuilder: RequestBuilder<GifDrawable>, listener: OnImageListener?): RequestBuilder<GifDrawable> {
        var builder = gifBuilder

        // 缩略图大小
        if (config.thumbnail > 0f) {
            builder.thumbnail(config.thumbnail)
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(config.thumbnailUrl)) {
            val thumbnailBuilder = Glide.with(context).asGif().load(obj).thumbnail(Glide.with(context).asGif().load(config.thumbnailUrl))
            builder = thumbnailBuilder
        }

        // 加载监听
        builder.listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message ?: "GlideImageStrategy：Gif load fail")
                return false
            }

            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource?.firstFrame)
                return false
            }
        })

        return builder
    }

    /**
     * 设置图片加载选项，返回请求对象
     */
    private fun buildOptions(context: Context, obj: Any, config: ImageConfig): RequestOptions {
        val options = RequestOptions()

        // 设置缓存策略，设置缓存策略要先判断是否有读写权限，如果没有权限，但是又设置了缓存策略则会加载失败
        val strategy = if (ImageUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            when (config.diskCacheStrategy.strategy) {
                ImageConfig.DiskCache.NONE.strategy -> DiskCacheStrategy.NONE
                ImageConfig.DiskCache.AUTOMATIC.strategy -> DiskCacheStrategy.AUTOMATIC
                ImageConfig.DiskCache.RESOURCE.strategy -> DiskCacheStrategy.RESOURCE
                ImageConfig.DiskCache.DATA.strategy -> DiskCacheStrategy.DATA
                ImageConfig.DiskCache.ALL.strategy -> DiskCacheStrategy.ALL
                else -> DiskCacheStrategy.RESOURCE
            }
        } else {
            DiskCacheStrategy.RESOURCE
        }
        options.diskCacheStrategy(strategy)


        // 设置加载优先级
        val priority = when (config.priority.priority) {
            ImageConfig.LoadPriority.LOW.priority -> Priority.LOW
            ImageConfig.LoadPriority.NORMAL.priority -> Priority.NORMAL
            ImageConfig.LoadPriority.HIGH.priority -> Priority.HIGH
            else -> Priority.NORMAL
        }
        options.priority(priority)


        // 内存缓存跳过
        options.skipMemoryCache(config.skipMemoryCache)

        // 占位图
        when {
        // 加载中占位图
            config.placeHolderResId != 0 ->
                options.placeholder(config.placeHolderResId)
        // 这里加载错误和链接为null都设置相同的占位图
            config.errorResId != 0 ->
                options.error(config.errorResId)
                        .fallback(config.errorResId)
        // 统一设置drawable占位图
            config.placeHolderDrawable != null ->
                options.placeholder(config.placeHolderDrawable)
                        .error(config.placeHolderDrawable)
        }

        // Tag
        val tag = config.tag
        if (tag != null) {
            options.signature(ObjectKey(tag))
        } else {
            options.signature(ObjectKey(obj.toString()))
        }

        return options
    }

}