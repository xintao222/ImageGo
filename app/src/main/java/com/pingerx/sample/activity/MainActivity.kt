package com.pingerx.sample.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.pingerx.imagego.core.ImageGo
import com.pingerx.imagego.core.listener.OnImageSaveListener
import com.pingerx.imagego.core.strategy.saveImage
import com.pingerx.imagego.glide.GlideImageStrategy
import com.pingerx.imagego.picasso.PicassoImageStrategy
import com.pingerx.sample.R
import com.pingerx.sample.data.DataProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutRes: Int = R.layout.activity_main) : BaseActivity() {


    override fun initView() {

    }


    override fun initEvent() {
        setClickListener(btnNormal)
        setClickListener(btnGif)
        setClickListener(btnSave)

    }

    override fun onClick(v: View) {
        when (v) {
            btnNormal -> startActivity(ImageNormalActivity::class.java)
            btnGif -> startActivity(ImageGifActivity::class.java)
            btnSave -> {
                if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    saveImage(this, DataProvider.getImageUrl(), listener = object : OnImageSaveListener {
                        override fun onSaveSuccess(path: String?, fileName: String) {
                            Toast.makeText(this@MainActivity, path, Toast.LENGTH_SHORT).show()
                        }

                        override fun onSaveStart() {

                        }


                        override fun onSaveFail(msg: String?) {
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
                    }
                }
            }
        }
    }


    private fun checkPermission(context: Context?, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            context?.packageManager?.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED
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
