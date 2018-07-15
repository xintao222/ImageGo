package com.fungo.imagego.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @author Pinger
 * @since 3/28/18 3:23 PM
 *
 * 图片加载库的配置，封装原始加载配置属性，进行转换
 */
class GlideOptions(private val builder: Builder) {

    /**
     * 解析配置
     */
    fun parseBuilder(config: GlideOptions): Builder {
        val builder = Builder()
        builder.placeHolderResId = config.getPlaceHolderResId()
        builder.placeHolderDrawable = config.getPlaceHolderDrawable()
        builder.errorResId = config.getErrorResId()
        builder.isCrossFade = config.isCrossFade()
        builder.tag = config.getTag()
        builder.asGif = config.isAsGif()
        builder.asBitmap = config.isAsBitmap()
        builder.skipMemoryCache = config.isSkipMemoryCache()
        builder.diskCacheStrategy = config.getDiskCacheStrategy()
        builder.thumbnail = config.getThumbnail()
        builder.thumbnailUrl = config.getThumbnailUrl()
        return builder
    }


    /**
     * 加载占位图资源ID
     */
    fun getPlaceHolderResId(): Int {
        return builder.placeHolderResId
    }

    /**
     * 加载占位图资源Drawable对象
     */
    fun getPlaceHolderDrawable(): Drawable? {
        return builder.placeHolderDrawable
    }

    /**
     * 错误占位图的资源ID
     */
    fun getErrorResId(): Int {
        return builder.errorResId
    }

    /**
     * 是否渐隐加载
     */
    fun isCrossFade(): Boolean {
        return builder.isCrossFade
    }

    /**
     * 图片加载的Tag
     */
    fun getTag(): String? {
        return builder.tag
    }

    /**
     * 是否是GIF图片
     */
    fun isAsGif(): Boolean {
        return builder.asGif
    }

    /**
     * 是否是Bitmap图片
     * 默认为bitmap图片
     */
    fun isAsBitmap(): Boolean {
        return builder.asBitmap
    }

    /**
     * 是否跳过内存缓存
     */
    fun isSkipMemoryCache(): Boolean {
        return builder.skipMemoryCache
    }

    /**
     * 缓存模式
     */
    fun getDiskCacheStrategy(): DiskCache {
        return builder.diskCacheStrategy
    }

    /**
     * 优先级
     */
    fun getPriority(): LoadPriority {
        return builder.priority
    }


    /**
     * 图片加载的缩略图比例
     */
    fun getThumbnail(): Float {
        return builder.thumbnail
    }

    /**
     * 缩略图的Url
     */
    fun getThumbnailUrl(): String? {
        return builder.thumbnailUrl
    }

    /**
     * Builder类
     */
    class Builder {
        var placeHolderResId = 0
        var placeHolderDrawable: Drawable? = null
        var errorResId = 0
        var isCrossFade = true
        var asGif: Boolean = false
        var asBitmap: Boolean = false
        var skipMemoryCache: Boolean = false
        var diskCacheStrategy = DiskCache.AUTOMATIC
        var priority = LoadPriority.NORMAL
        var thumbnail: Float = 0f
        var thumbnailUrl: String? = null
        var tag: String? = null

        fun setPlaceHolderResId(placeHolderResId: Int): Builder {
            this.placeHolderResId = placeHolderResId
            return this
        }

        fun setPlaceHolderDrawable(placeHolderDrawable: Drawable): Builder {
            this.placeHolderDrawable = placeHolderDrawable
            return this
        }

        fun setErrorResId(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        fun isCrossFade(isCrossFade: Boolean): Builder {
            this.isCrossFade = isCrossFade
            return this
        }

        fun setAsGif(asGif: Boolean): Builder {
            this.asGif = asGif
            return this
        }

        fun setAsBitmap(asBitmap: Boolean): Builder {
            this.asBitmap = asBitmap
            return this
        }

        fun isSkipMemoryCache(skipMemoryCache: Boolean): Builder {
            this.skipMemoryCache = skipMemoryCache
            return this
        }

        fun setDiskCacheStrategy(diskCacheStrategy: DiskCache): Builder {
            this.diskCacheStrategy = diskCacheStrategy
            return this
        }

        fun setTag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun setPriority(priority: LoadPriority): Builder {
            this.priority = priority
            return this
        }

        fun setThumbnail(thumbnail: Float): Builder {
            this.thumbnail = thumbnail
            return this
        }

        fun setThumbnailUrl(thumbnailUrl: String): Builder {
            this.thumbnailUrl = thumbnailUrl
            return this
        }

        fun build(): GlideOptions {
            return GlideOptions(this)
        }
    }


    /**
     * 硬盘缓存策略
     */
    enum class DiskCache(val strategy: DiskCacheStrategy) {
        /**
         * 没有缓存
         */
        NONE(DiskCacheStrategy.NONE),

        /**
         * 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略
         */
        AUTOMATIC(DiskCacheStrategy.AUTOMATIC),

        /**
         * 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源
         */
        RESOURCE(DiskCacheStrategy.RESOURCE),

        /**
         * 在资源解码前就将原始数据写入磁盘缓存
         */
        DATA(DiskCacheStrategy.DATA),

        /**
         * 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据
         */
        ALL(DiskCacheStrategy.ALL)
    }

    /**
     * 加载优先级策略
     * 指定了图片加载的优先级后，加载时会按照图片的优先级进行顺序加载
     * IMMEDIATE优先级时会直接加载，不需要等待
     */
    enum class LoadPriority(val strategy: Priority) {
        /**
         * 低优先级
         */
        LOW(Priority.LOW),
        /**
         * 普通优先级
         */
        NORMAL(Priority.NORMAL),
        /**
         * 高优先级
         */
        HIGH(Priority.HIGH),

        /**
         * 立即加载，无需等待
         */
        IMMEDIATE(Priority.IMMEDIATE)
    }
}