package com.fungo.imagego.utils

/**
 * @author Pinger
 * @since 18-6-12 下午2:51
 * 图片加载常用字符常量
 */

object ImageConstant {

    const val IMAGE_GIF = ".gif"
    const val IMAGE_JPG = ".jpg"
    const val IMAGE_PATH = "Fungo"
    const val IMAGE_PLACE_HOLDER_COLOR = "#F2F2F2"

    const val ERROR_LOAD_NULL_STRATEGY = "图片加载失败：加载策略为null，已使用默认策略，请在Application中设置图片加载策略"
    const val ERROR_LOAD_NULL_CONTEXT_ANY = "图片加载失败：context为null或者any为null"

    const val SAVE_NULL_CONTEXT_ANY = "图片保存失败：context为null或者any为null"
    const val SAVE_FAIL = "图片保存失败"
    const val SAVE_PATH= "图片已保存至 "

    const val CLEAR_NULL_CONTEXT = "清理失败：context为null"


}