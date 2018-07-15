package com.fungo.sample

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fungo.imagego.ImageManager
import com.fungo.imagego.listener.OnImageListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val mUrl = "http://img.mp.itc.cn/upload/20161121/d30e0a4a1f8b418f92e973310885e4ee_th.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImageManager.instance.loadImage(mUrl,imageView,object :OnImageListener{
            override fun onSuccess(bitmap: Bitmap?) {
                print("success")
            }

            override fun onFail(msg: String?) {
                print(msg)
            }
        })
    }
}
