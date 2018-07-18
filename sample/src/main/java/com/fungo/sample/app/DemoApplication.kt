package com.fungo.sample.app

import android.app.Application
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.setAutoGif
import com.fungo.imagego.setDebug
import com.fungo.imagego.setImageStrategy
import com.github.moduth.blockcanary.BlockCanary
import com.squareup.leakcanary.LeakCanary

/**
 * @author Pinger
 * @since 2018/7/15 下午3:21
 *
 */
class DemoApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initTest()
        initImageStrategy()
    }

    private fun initTest() {
        // 内存泄露
        LeakCanary.install(this)
        // UI卡顿检测
        BlockCanary.install(this, AppBlockCanaryContext()).start()
    }

    private fun initImageStrategy() {
        setDebug(true)
        setAutoGif(true)
        setImageStrategy(GlideImageStrategy())
    }

}