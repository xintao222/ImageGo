package com.fungo.imagego.glide

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.fungo.imagego.create.ImageStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
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
    private val defaultOptions = GlideImageOptions
            .Builder()
            .setAsBitmap(true)
            .setPlaceHolderDrawable(ColorDrawable(Color.parseColor(ImageConstant.IMAGE_PLACE_HOLDER_COLOR)))
            .setDiskCacheStrategy(GlideImageOptions.DiskCache.AUTOMATIC)
            .setPriority(GlideImageOptions.LoadPriority.NORMAL)
            .isCrossFade(true)
            .build()


    override fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, null)
    }

    override fun loadImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultOptions.parseBuilder(defaultOptions)
                .setPlaceHolderResId(placeholder).build(), null)
    }


    override fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        loadImage(url, imageView, defaultOptions, listener)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?) {
        loadGifImage(url, imageView, null)
    }

    override fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultOptions.parseBuilder(defaultOptions)
                .setAsGif(true).setAsBitmap(false).build(), null)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        loadImage(url, imageView, defaultOptions.parseBuilder(defaultOptions)
                .setAsGif(true).setAsBitmap(false).build(), listener)
    }

    override fun loadImage(obj: Any?, imageView: ImageView?) {
        loadImage(obj, imageView, defaultOptions, null)
    }


    override fun loadImageNoFade(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, defaultOptions.parseBuilder(defaultOptions)
                .isCrossFade(false).build(), null)
    }

    override fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?) {
        ImageUtils.runOnSubThread(Runnable {
            try {
                if (context != null && !TextUtils.isEmpty(url)) {
                    val suffix = if (ImageUtils.isGif(url)) {
                        "${System.currentTimeMillis()}.gif"
                    } else {
                        "${System.currentTimeMillis()}.jpg"
                    }

                    val destFile = File(ImageUtils.getImageSavePath(context) + suffix)
                    val imageFile = download(context, url!!)
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

    override fun loadBitmapImage(context: Context?, url: String?, listener: OnImageListener?) {
        if (context == null || TextUtils.isEmpty(url)) {
            listener?.onFail("context or url is null...")
            return
        }
        ImageUtils.runOnSubThread(Runnable {
            val bitmap = Glide.with(context).asBitmap().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            ImageUtils.runOnUIThread(Runnable {
                listener?.onSuccess(bitmap)
            })
        })
    }

    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearDiskCache()
        }
    }

    /** 清除内存缓存，只能在主线程调用本方法 */
    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        }
    }

    override fun clearImageCache(context: Context?) {
        ImageUtils.showToast(context, "正在清除缓存...")
        clearImageMemoryCache(context)
        ImageUtils.runOnSubThread(Runnable {
            clearImageDiskCache(context)
            ImageUtils.showToast(context, "清除成功")
        })
    }

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

    private fun loadImage(obj: Any?, imageView: ImageView?, config: GlideImageOptions?, listener: OnImageListener?) {
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

        if (imageView == null) {
            listener?.onFail("GlideImageStrategy：imageView is null...")
            return
        }

        val context = imageView.context
        if (context == null) {
            listener?.onFail("GlideImageStrategy：context is null...")
            return
        }
        val glideConfig: GlideImageOptions = config ?: defaultOptions
        try {
            when {
                glideConfig.isAsGif() -> {
                    val gifBuilder = Glide.with(context).asGif().load(obj)
                    val builder = buildGift(context, obj, glideConfig, gifBuilder, listener)
                    // 使用clone方法复用builder，不会请求网络
                    builder.clone().apply(buildOptions(context, obj, glideConfig)).into(imageView)
                }
                glideConfig.isAsBitmap() -> {
                    val bitmapBuilder = Glide.with(context).asBitmap().load(obj)
                    val builder = buildBitmap(context, obj, glideConfig, bitmapBuilder, listener)
                    builder.clone().apply(buildOptions(context, obj, glideConfig)).into(imageView)
                }
            }
        } catch (e: Exception) {
            listener?.onFail("GlideImageStrategy：load image exception: " + e.message)
            imageView.setImageResource(glideConfig.getErrorResId())
        }
    }


    /**
     * 设置bitmap属性
     */
    private fun buildBitmap(context: Context, obj: Any, glideConfig: GlideImageOptions, bitmapBuilder: RequestBuilder<Bitmap>, listener: OnImageListener?): RequestBuilder<Bitmap> {
        var builder = bitmapBuilder
        // 渐变展示
        if (glideConfig.isCrossFade()) {
            builder.transition(BitmapTransitionOptions.withCrossFade())
        }

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

        // 缩略图大小
        if (glideConfig.getThumbnail() > 0f) {
            builder.thumbnail(glideConfig.getThumbnail())
        }


        // 缩略图请求
        if (!TextUtils.isEmpty(glideConfig.getThumbnailUrl())) {
            val thumbnailBuilder = Glide.with(context).asBitmap().load(obj).thumbnail(Glide.with(context).asBitmap().load(glideConfig.getThumbnailUrl()))
            builder = thumbnailBuilder
        }
        return builder
    }


    /**
     * 设置Gift属性
     */
    private fun buildGift(context: Context, obj: Any, glideConfig: GlideImageOptions, gifBuilder: RequestBuilder<GifDrawable>, listener: OnImageListener?): RequestBuilder<GifDrawable> {
        var builder = gifBuilder

        // 渐变展示
        if (glideConfig.isCrossFade()) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }

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

        // 缩略图大小
        if (glideConfig.getThumbnail() > 0f) {
            builder.thumbnail(glideConfig.getThumbnail())
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(glideConfig.getThumbnailUrl())) {
            val thumbnailBuilder = Glide.with(context).asGif().load(obj).thumbnail(Glide.with(context).asGif().load(glideConfig.getThumbnailUrl()))
            builder = thumbnailBuilder
        }
        return builder
    }

    /**
     * 设置图片加载选项并且加载图片
     */
    private fun buildOptions(context: Context, obj: Any, glideConfig: GlideImageOptions): RequestOptions {
        val options = RequestOptions()

        // 设置缓存策略，设置缓存策略要先判断是否有读写权限，如果没有权限，但是又设置了缓存策略则会加载失败
        val cacheStrategy = if (ImageUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            glideConfig.getDiskCacheStrategy()
        } else {
            GlideImageOptions.DiskCache.RESOURCE
        }
        options.diskCacheStrategy(cacheStrategy.strategy)

        // 占位图
        when {
            glideConfig.getPlaceHolderResId() != 0 ->
                options.placeholder(glideConfig.getPlaceHolderResId())
            glideConfig.getErrorResId() != 0 ->
                options.error(glideConfig.getErrorResId())
                        .fallback(glideConfig.getErrorResId())
            glideConfig.getPlaceHolderDrawable() != null ->
                options.placeholder(glideConfig.getPlaceHolderDrawable())
                        .error(glideConfig.getPlaceHolderDrawable())
        }

        // 优先级和内存缓存跳过
        options
                .priority(glideConfig.getPriority().strategy)
                .skipMemoryCache(glideConfig.isSkipMemoryCache())

        // Tag
        val tag = glideConfig.getTag()
        if (tag != null) {
            options.signature(ObjectKey(tag))
        } else {
            options.signature(ObjectKey(obj.toString()))
        }

        return options
    }

    /** 获取图片的缓存文件 */
    override fun download(context: Context, url: String): File {
        return Glide.with(context).download(url).submit().get()
    }
}