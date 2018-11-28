package com.fungo.sample.activity

import com.fungo.imagego.strategy.loadImage
import com.fungo.sample.R
import com.fungo.sample.data.DataProvider
import kotlinx.android.synthetic.main.activity_btn_gif.*

/**
 * @author Pinger
 * @since 18-11-28 下午3:27
 */
class ImageGifActivity(override val layoutRes: Int = R.layout.activity_btn_gif) : BaseActivity() {

    override fun getTitleRes(): Int = R.string.image_gif

    override fun isHomeAsUpEnabled(): Boolean = true

    override fun initView() {
        loadImage(DataProvider.getGifUrl(), imageView)
    }
}