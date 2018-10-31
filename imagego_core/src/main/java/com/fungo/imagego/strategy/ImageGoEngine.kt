package com.fungo.imagego.strategy

/**
 * @author Pinger
 * @since 18-7-19 下午3:16
 * 图片加载的配置引擎
 *
 */

object ImageGoEngine {

    /**
     * 初始化加载策略，在Application中设置
     * 默认使用Glide加载策略
     */
    private var mStrategy: ImageStrategy? = null

    /**
     * 是否是开发模式，正式环境设置为false
     */
    private var isDebug = true


    fun isDebug(): Boolean {
        return isDebug
    }


    fun getStrategy(): ImageStrategy {
        if (mStrategy == null) {
            throw NullPointerException("ImageStrategy can not be null,please call ImageGoEngine.setImageStrategy() first.")
        }
        return mStrategy!!
    }

    fun setDebug(isDebug: Boolean): ImageGoEngine {
        this.isDebug = isDebug
        return this
    }

    fun setImageStrategy(strategy: ImageStrategy): ImageGoEngine {
        this.mStrategy = strategy
        return this
    }

}