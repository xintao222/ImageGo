package com.fungo.sample.app

import android.app.Application
import com.fungo.imagego.ImageGo
import com.fungo.imagego.picasso.PicassoImageStrategy

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
                .setImageStrategy(PicassoImageStrategy())
    }
}