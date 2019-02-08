package com.pingerx.sample.app

import android.app.Application
import com.pingerx.imagego.core.ImageGo
import com.pingerx.imagego.glide.GlideImageStrategy

/**
 * @author Pinger
 * @since 2018/7/15 下午3:21
 *
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initImageStrategy()
    }


    private fun initImageStrategy() {
        ImageGo.setDebug(true)
                .setStrategy(GlideImageStrategy())
    }
}