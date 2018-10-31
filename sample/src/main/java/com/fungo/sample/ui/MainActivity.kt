package com.fungo.sample.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.fungo.imagego.*
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.sample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private val mUrl = "http://img.mp.itc.cn/upload/20161121/d30e0a4a1f8b418f92e973310885e4ee_th.jpg"
    private val mGif = "http://www.gaoxiaogif.com/d/file/201712/ac2cba0163c27c0f455c22df35794bc8.gif"

    private var isGif = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rgType.setOnCheckedChangeListener { _, checkedId ->
            isGif = checkedId == R.id.rbGif
        }

        rbNormal.isChecked = true
        rbGlide.isChecked = true

        initEvent()

        onLoadRound()
    }

    private fun initEvent() {
        tvBitmap.setOnClickListener(this)
        tvOrigin.setOnClickListener(this)
        tvCircle.setOnClickListener(this)
        tvRound.setOnClickListener(this)
        tvBlur.setOnClickListener(this)
        tvProgress.setOnClickListener(this)
        tvSave.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v) {
            tvBitmap -> onLoadBitmap()
            tvOrigin -> onLoadOrigin()
            tvCircle -> onLoadCircle()
            tvRound -> onLoadRound()
            tvBlur -> onLoadBlur()
            tvProgress -> onLoadProgress()
            tvSave -> onLoadSave()
        }
    }

    private fun getUrl(): String {
        return if (isGif) {
            mGif
        } else mUrl
    }

    fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onLoadOrigin() {
        isCircle(false)
        loadImage(getUrl(), imageView)
    }


    private fun onLoadBitmap() {
        isCircle(false)
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


    private fun onLoadCircle() {
        isCircle(true)
        loadCircle(getUrl(), circleImage)
    }


    private fun onLoadRound() {
        isCircle(false)
        loadRound(getUrl(), imageView, 24)
    }

    private fun onLoadBlur() {
        isCircle(false)
        loadBlur(getUrl(), imageView)
    }


    private fun isCircle(isCircle: Boolean) {
        if (isCircle) {
            circleImage.visibility = View.VISIBLE
            imageView.visibility = View.GONE
        } else {
            circleImage.visibility = View.GONE
            imageView.visibility = View.VISIBLE
        }
    }


    /**
     * 展示进度条
     */
    private fun onLoadProgress() {
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
    private fun onLoadSave() {
        saveImage(this, getUrl())
    }

}
