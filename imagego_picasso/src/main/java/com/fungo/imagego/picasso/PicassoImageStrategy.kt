package com.fungo.imagego.picasso

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.picasso.transform.BlurTransformation
import com.fungo.imagego.picasso.transform.CircleTransformation
import com.fungo.imagego.picasso.transform.RoundedCornersTransformation
import com.fungo.imagego.strategy.ImageOptions
import com.fungo.imagego.strategy.ImageStrategy
import com.fungo.imagego.utils.ImageConstant
import com.fungo.imagego.utils.ImageUtils
import com.squareup.picasso.*
import com.squareup.picasso.Target
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author Pinger
 * @since 18-10-31 下午3:23
 *
 * Picasso加载策略，实现了大部分基本的功能
 * 加载网络图片，圆形边框头像，圆角图片，高斯模糊图片，保存图片，加载Bitmap，加载监听等
 *
 * 注意：
 * 1,由于Picasso和Gide和很多不一样的地方，所以有些功能没Glide全
 * 2,Picasso自身不支持加载Gif图，保存的Gif图片也是静态图
 *
 */
class PicassoImageStrategy : ImageStrategy {

    /**
     * 获取默认的配置,可以手动配置
     * 使用默认的加载和加载失败的占位图
     * 设置缓存策略为默认策略
     * 设置加载优先级为普通优先级
     * 设置加载渐变动画
     * 设置自动加载Gif图
     */
    override fun getDefaultBuilder(): ImageOptions.Builder {
        return ImageOptions
                .Builder()
                .setPlaceHolderDrawable(ColorDrawable(Color.parseColor(ImageConstant.IMAGE_PLACE_HOLDER_COLOR)))
                .setDiskCacheStrategy(ImageOptions.DiskCache.AUTOMATIC)
                .setPriority(ImageOptions.LoadPriority.NORMAL)
                .setCrossFade(true)
                .setAutoGif(true)
    }


    /**
     * 加载图片的主要方法
     * 由于传入了加载的图片，所以这里成功的回调传入null，picasso的回调里没有bitmap
     */
    override fun loadImage(any: Any?, view: View?, listener: OnImageListener?, options: ImageOptions) {
        // any和view判空
        if (any == null || view == null) {
            listener?.onFail(ImageConstant.LOAD_NULL_ANY_VIEW)
            ImageUtils.logD(ImageConstant.LOAD_NULL_ANY_VIEW)
            return
        }

        // context判空
        val context = view.context
        if (context == null) {
            listener?.onFail(ImageConstant.LOAD_NULL_CONTEXT)
            ImageUtils.logD(ImageConstant.LOAD_NULL_CONTEXT)
            return
        }

        if (view is ImageView) {
            try {
                generateRequestOptions(view, getRequestCreator(any), options)
                        .into(view, object : Callback {
                            override fun onSuccess() {
                                listener?.onSuccess(null)
                            }

                            override fun onError(e: Exception?) {
                                listener?.onFail(e?.message)
                            }
                        })
            } catch (e: java.lang.Exception) {
                listener?.onFail(ImageConstant.LOAD_ERROR + "：" + e.message)
                view.setImageResource(options.errorResId)
            }
        }
    }


    override fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener) {
        getRequestCreator(any).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                listener.onFail(e?.message)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                listener.onSuccess(bitmap)
            }
        })

    }

    override fun loadBitmap(context: Context?, any: Any?): Bitmap? {
        return getRequestCreator(any).get()
    }

    override fun loadProgress(any: Any?, view: View?, listener: OnProgressListener) {

    }

    override fun saveImage(context: Context?, any: Any?, path: String?, listener: OnImageSaveListener?) {
        listener?.onSaveStart()
        if (context == null || any == null) {
            listener?.onSaveFail(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            ImageUtils.logD(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            return
        }

        if(!ImageUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            listener?.onSaveFail(ImageConstant.SAVE_NOT_PERMISSION)
            return
        }
        ImageUtils.runOnSubThread(Runnable {
            try {
                // 图片后缀
                val suffix = if (ImageUtils.isGif(any)) {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_GIF
                } else {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_JPG
                }

                val filePath = if (TextUtils.isEmpty(path)) {
                    ImageUtils.getImageSavePath(context)
                } else path + File.separator

                // 保存的位置
                val destFile = File(filePath + suffix)
                // 要保存的原图
                val imageFile = downloadImage(context, any)
                // 进行保存
                val isCopySuccess = ImageUtils.copyFile(imageFile, destFile)

                // 最后通知图库更新
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(destFile)))
                // 主线程回调
                ImageUtils.runOnUIThread(Runnable {
                    if (isCopySuccess) {
                        if (listener == null) {
                            ImageUtils.showToast(context, ImageConstant.SAVE_PATH + filePath, Toast.LENGTH_LONG)
                        } else {
                            listener.onSaveSuccess(ImageConstant.SAVE_PATH + ImageUtils.getImageSavePath(context))
                        }
                    } else {
                        if (listener == null) {
                            ImageUtils.showToast(context, ImageConstant.SAVE_FAIL)
                        } else {
                            listener.onSaveFail(ImageConstant.SAVE_FAIL)
                        }
                    }
                })
            } catch (e: Exception) {
                ImageUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail(ImageConstant.SAVE_FAIL)
                    ImageUtils.logE(ImageConstant.SAVE_FAIL + ": " + e.message)
                })
            }
        })
    }


    override fun clearImageDiskCache(context: Context?) {
    }

    override fun clearImageMemoryCache(context: Context?) {
    }

    override fun getCacheSize(context: Context?): String {
        return "0M"
    }

    override fun resumeRequests(context: Context?) {
    }

    override fun pauseRequests(context: Context?) {


    }


    /**
     * 同步操作，需要在子线程执行
     */
    override fun downloadImage(context: Context, any: Any?): File {
        // 将要保存图片的路径
        val file = File(ImageUtils.getImageCacheDir(context).path +
                File.separator + System.currentTimeMillis().toString() + ".cache")
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            getRequestCreator(any).get().compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


    /**
     * 配置请求的参数
     */
    private fun generateRequestOptions(view: View, request: RequestCreator, options: ImageOptions): RequestCreator {

        val context = view.context

        // 占位图
        if (options.placeHolderDrawable != null) request.placeholder(options.placeHolderDrawable!!)
        if (options.placeHolderResId != 0) request.error(options.placeHolderResId)
        if (options.errorDrawable != null) request.error(options.errorDrawable!!)
        if (options.errorResId != 0) request.error(options.errorResId)


        // 标签
        if (!TextUtils.isEmpty(options.tag)) {
            request.tag(options.tag!!)
        }

        // 指定图片大小
        if (options.size != null) {
            request.resize(options.size!!.width, options.size!!.height)
        }

        // 高斯模糊
        if (options.isBlur) {
            request.transform(BlurTransformation(context, options.blurRadius, options.blurSampling))
        }

        // 圆形图片
        if (options.isCircle) {
            request.transform(CircleTransformation(options.circleBorderWidth, options.circleBorderColor))
        }

        // 圆角图片
        if (options.isRoundedCorners) {
            // 如果是圆角图片，就保持原来大小，否则会裁剪出问题
            // 而且fit属性会让ImageView的ScaleType失去作用
            request.fit()
            request.transform(RoundedCornersTransformation(options.roundRadius, options.roundType))
        }

        // 默认有过度动画
        if (!options.isCrossFade) {
            request.noFade()
        }

        // 加载优先级
        when (options.priority) {
            ImageOptions.LoadPriority.HIGH -> request.priority(Picasso.Priority.HIGH)
            ImageOptions.LoadPriority.LOW -> request.priority(Picasso.Priority.LOW)
            else -> request.priority(Picasso.Priority.NORMAL)
        }

        // 是否跳过内存缓存
        if (options.skipMemoryCache) {
            request.memoryPolicy(MemoryPolicy.NO_CACHE)
        }
        return request
    }


    /**
     * 获取请求参数构造器
     */
    private fun getRequestCreator(any: Any?): RequestCreator {
        return when (any) {
            is String -> Picasso.get().load(any)
            is Uri -> Picasso.get().load(any)
            is File -> Picasso.get().load(any)
            is Int -> Picasso.get().load(any)
            else -> throw IllegalStateException("Picasso does not support loading this type of resource.")
        }
    }


}