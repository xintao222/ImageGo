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


    override fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener?) {
        if (context == null || any == null) {
            listener?.onFail("context or url is null...")
            return
        }
        ImageUtils.runOnSubThread(Runnable {
            val bitmap = Glide.with(context).asBitmap().load(any).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            ImageUtils.runOnUIThread(Runnable {
                listener?.onSuccess(bitmap)
            })
        })
    }

    override fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?) {
        ImageUtils.runOnSubThread(Runnable {
            try {
                if (context != null && any != null) {
                    val suffix = if (ImageUtils.isGif(any)) {
                        "${System.currentTimeMillis()}.gif"
                    } else {
                        "${System.currentTimeMillis()}.jpg"
                    }

                    val destFile = File(ImageUtils.getImageSavePath(context) + suffix)
                    val imageFile = download(context, any)
                    val isCopySuccess = ImageUtils.copyFile(imageFile, destFile)

                    // 最后通知图库更新
                    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(destFile)))
                    ImageUtils.runOnUIThread(Runnable {
                        if (isCopySuccess) {
                            listener?.onSaveSuccess("图片已保存至 " + ImageUtils.getImageSavePath(context))
                        } else {
                            listener?.onSaveFail("保存失败")
                        }
                    })
                }
            } catch (e: Exception) {
                ImageUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail("保存失败")
                })
            }
        })
    }


    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearDiskCache()
        }
    }

    /**
     * 清除内存缓存，只能在主线程调用本方法
     */
    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        }
    }


    /**
     * 获取本地缓存大小
     */
    override fun getCacheSize(context: Context?): String {
        return ImageUtils.getImageCacheSize(context)
    }

    override fun resumeRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).resumeRequests()
        }
    }

    override fun pauseRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).pauseRequests()
        }
    }

    private fun loadImage(obj: Any?, view: View?, config: ImageConfig, listener: OnImageListener?) {
        if (obj == null) {
            listener?.onFail("GlideImageStrategy：image request url is null...")
            return
        }

        if (obj is String) {
            if (TextUtils.isEmpty(obj)) {
                listener?.onFail("GlideImageStrategy：image request url is null...")
                return
            }
        }

        if (view == null) {
            listener?.onFail("GlideImageStrategy：imageView is null...")
            return
        }

        val context = view.context
        if (context == null) {
            listener?.onFail("GlideImageStrategy：context is null...")
            return
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
     * 设置图片加载选项并且加载图片
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

    /**
     * 获取图片的缓存文件
     */
    override fun download(context: Context, any: Any?): File {
        return Glide.with(context).download(any).submit().get()
    }
}