package com.fungo.sample.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Pinger
 * @since 18-11-28 下午12:56
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initPre()
        initView()
        initEvent()
    }

    private fun initPre() {
        if (getTitleRes() != 0) {
            setTitle(getTitleRes())
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(isHomeAsUpEnabled())
    }


    abstract val layoutRes: Int

    abstract fun initView()
    protected open fun initEvent() {}
    protected open fun setClickListener(view: View?) {
        view?.setOnClickListener(this)
    }

    protected open fun isHomeAsUpEnabled() = false
    protected open fun getTitleRes(): Int = 0

    override fun onClick(v: View) {

    }


    // -------------------- menu -----------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (getMenuRes() != 0) {
            MenuInflater(this).inflate(getMenuRes(), menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    protected open fun getMenuRes(): Int = 0

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        } else {
            onMenuSelected(item!!)
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun onMenuSelected(item: MenuItem) {}

    protected open fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    protected open fun showToast(@StringRes resId: Int) {
        showToast(getString(resId))
    }

    protected open fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}