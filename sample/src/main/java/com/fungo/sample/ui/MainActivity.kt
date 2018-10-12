package com.fungo.sample.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.fungo.imagego.*
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.strategy.ImageGoEngine
import com.fungo.sample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mUrl = "http://img.mp.itc.cn/upload/20161121/d30e0a4a1f8b418f92e973310885e4ee_th.jpg"
    private val mGif = "http://www.gaoxiaogif.com/d/file/201712/ac2cba0163c27c0f455c22df35794bc8.gif"

    private var isGif = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onLoadRound(imageView)

        rgStrategy.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGlide -> ImageGoEngine.setImageStrategy(GlideImageStrategy())
            }
        }

        rgType.setOnCheckedChangeListener { _, checkedId ->
            // 手动区分链接
            isGif = checkedId == R.id.rbGif
            // 自动设置区分gif加载
            ImageGoEngine.setAutoGif(checkedId == R.id.rbGif)
        }

        rbNormal.isChecked = true
        rbGlide.isChecked = true
    }


    private fun getUrl(): String {
        return if (isGif) {
            mGif
        } else mUrl
    }

    fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun onLoadOrigin(view: View) {
        loadImage(getUrl(), imageView)
    }


    fun onLoadBitmap(view: View) {
        loadBitmap(this, getUrl(), object : OnImageListener {
            override fun onSuccess(bitmap: Bitmap?) {
                showToast("Bitmap加载成功")
                imageView.setImageBitmap(bitmap)
            }

            override fun onFail(msg: String?) {
                showToast(msg)
            }

        })
    }


    fun onLoadCircle(view: View) {
        loadCircle(getUrl(), imageView)
    }


    fun onLoadRound(view: View) {
        loadRound(getUrl(), imageView, 48)
    }

    fun onLoadBlur(view: View) {
        loadBlur(getUrl(), imageView)
    }


    /**
     * 展示进度条
     */
    fun onLoadProgress(view: View) {
        loadProgress(getUrl(), imageView, object : OnProgressListener {
            override fun onProgress(bytesRead: Long, contentLength: Long, isFinish: Boolean) {
                progressView.visibility = if (isFinish) View.GONE else View.VISIBLE
                progressView.progress = (100f * bytesRead / contentLength).toInt()
            }
        })
    }


    /**
     * 保存图片
     */
    fun onLoadSave(view: View) {
        saveImage(this, getUrl())
    }

}
