package com.fungo.sample.activity

import android.view.MenuItem
import android.view.View
import com.fungo.imagego.ImageGo
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.picasso.PicassoImageStrategy
import com.fungo.sample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutRes: Int = R.layout.activity_main) : BaseActivity() {


    override fun initView() {

    }


    override fun initEvent() {
        setClickListener(btnNormal)
        setClickListener(btnGif)

    }

    override fun onClick(v: View) {
        when (v) {
            btnNormal -> startActivity(ImageNormalActivity::class.java)
            btnGif -> startActivity(ImageGifActivity::class.java)

        }

    }

    override fun getMenuRes(): Int = R.menu.menu_main


    override fun onMenuSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.main_glide -> {
                showToast(R.string.app_strategy_glide)
                ImageGo.setStrategy(GlideImageStrategy())
            }
            R.id.main_picasso -> {
                showToast(R.string.app_strategy_picasso)
                ImageGo.setStrategy(PicassoImageStrategy())
            }
        }
    }
}
