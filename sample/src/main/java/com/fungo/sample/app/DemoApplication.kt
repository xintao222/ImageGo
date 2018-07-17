package com.fungo.sample.app

import android.app.Application
import com.fungo.imagego.ImageManager
import com.fungo.imagego.glide.GlideImageStrategy

/**
 * @author Pinger
 * @since 2018/7/15 下午3:21
 *
 */
class DemoApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        ImageManager.instance.setImageGoStrategy(GlideImageStrategy())
    }

}