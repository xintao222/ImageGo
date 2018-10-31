package com.fungo.sample.app

import android.app.Application
import com.fungo.imagego.picasso.PicassoImageStrategy
import com.fungo.imagego.strategy.ImageGoEngine

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
        ImageGoEngine
                .setDebug(true)
                .setImageStrategy(PicassoImageStrategy())


    }

}