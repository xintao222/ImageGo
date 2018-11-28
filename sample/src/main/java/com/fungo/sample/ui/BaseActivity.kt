package com.fungo.sample.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * @author Pinger
 * @since 18-11-28 下午12:56
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initView()
        initEvent()
    }


    abstract val layoutRes: Int

    abstract fun initView()
    protected open fun initEvent() {}
    protected open fun setClickListener(view: View?) {
        view?.setOnClickListener(this)
    }

    override fun onClick(v: View) {

    }
}