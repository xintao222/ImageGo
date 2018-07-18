package com.fungo.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.fungo.imagego.*
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.glide.transform.RoundType
import com.fungo.imagego.listener.OnImageSaveListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mUrl = "http://img.mp.itc.cn/upload/20161121/d30e0a4a1f8b418f92e973310885e4ee_th.jpg"
    private val mGif = "http://www.gaoxiaogif.com/d/file/201712/ac2cba0163c27c0f455c22df35794bc8.gif"

    private var isGif = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadRound(mUrl, imageView, 12)

        rbNormal.isChecked = true
        rbGlide.isChecked = true

        rgStrategy.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGlide -> setImageStrategy(GlideImageStrategy())
            }
        }

        rgType.setOnCheckedChangeListener { _, checkedId ->
            // 手动区分链接
            isGif = checkedId == R.id.rbGif
            // 自动设置区分gif加载
            setAutoGif(checkedId == R.id.rbGif)
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


    fun onLoadCircle(view: View) {
        loadCircle(getUrl(), imageView)
    }

    fun onLoadRound(view: View) {
        loadRound(getUrl(), imageView, 24)
    }


    fun onLoadBlur(view: View) {
        loadBlur(getUrl(), imageView)
    }

    fun onLoadProgress(view: View) {

    }

    fun onLoadSave(view: View) {
        saveImage(this, getUrl(), object : OnImageSaveListener {
            override fun onSaveSuccess(path: String?) {

            }

            override fun onSaveFail(msg: String?) {

            }
        })
    }



}
