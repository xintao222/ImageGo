package com.pingerx.sample.activity

import com.pingerx.imagego.core.strategy.loadImage
import com.pingerx.sample.R
import com.pingerx.sample.data.DataProvider
import kotlinx.android.synthetic.main.activity_btn_normal.*

/**
 * @author Pinger
 * @since 18-11-28 下午3:06
 */
class ImageNormalActivity(override val layoutRes: Int = R.layout.activity_btn_normal) : BaseActivity() {

    override fun getTitleRes(): Int = R.string.image_normal

    override fun isHomeAsUpEnabled(): Boolean = true

    override fun initView() {
        loadImage(DataProvider.getImageUrl(), imageView)
    }
}