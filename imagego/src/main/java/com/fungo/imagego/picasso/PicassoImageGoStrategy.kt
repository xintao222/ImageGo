package com.fungo.imagego.picasso

import android.content.Context
import android.widget.ImageView
import com.fungo.imagego.create.ImageGoStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import java.io.File

/**
 * @author Pinger
 * @since 2018/5/13 下午4:13
 *
 */
class PicassoImageGoStrategy : ImageGoStrategy {

    override fun loadBitmapImage(context: Context?, url: String?, listener: OnImageListener?) {
    }

    override fun loadImageNoFade(url: String?, imageView: ImageView?) {
    }


    override fun loadImage(url: String?, imageView: ImageView?) {
    }

    override fun loadImage(url: String?, placeholder: Int, imageView: ImageView?) {
    }

    override fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
    }

    override fun loadGifImage(url: String?, imageView: ImageView?) {
    }

    override fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?) {
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
    }

    override fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?) {
    }

    override fun loadImage(obj: Any?, imageView: ImageView?) {
    }

    override fun clearImageDiskCache(context: Context?) {
    }

    override fun clearImageMemoryCache(context: Context?) {
    }

    override fun clearImageCache(context: Context?) {
    }

    override fun getCacheSize(context: Context?): String {
        return ""
    }

    override fun resumeRequests(context: Context?) {
    }

    override fun pauseRequests(context: Context?) {
    }

    override fun download(context: Context, url: String): File {
        return File("")
    }


}