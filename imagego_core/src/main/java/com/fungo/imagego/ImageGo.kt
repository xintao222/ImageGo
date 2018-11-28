package com.fungo.imagego

import com.fungo.imagego.strategy.ImageStrategy


object ImageGo {

    /**
     * 初始化加载策略，在Application中设置
     * 默认使用Glide加载策略
     */
    private var mStrategy: ImageStrategy? = null

    /**
     * 是否是开发模式，正式环境设置为false
     */
    private var isDebug = true

    fun setDebug(isDebug: Boolean): ImageGo {
        this.isDebug = isDebug
        return this
    }

    fun isDebug(): Boolean {
        return isDebug
    }


    fun getStrategy(): ImageStrategy {
        if (mStrategy == null) {
            throw NullPointerException("ImageStrategy can not be null,please call ImageGoEngine.setStrategy() first.")
        }
        return mStrategy!!
    }

    fun setStrategy(strategy: ImageStrategy): ImageGo {
        this.mStrategy = strategy
        return this
    }

}















