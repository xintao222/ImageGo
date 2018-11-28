
# ImageGo(二次图片加载框架)
[![](https://www.jitpack.io/v/PingerOne/ImageGo.svg)](https://www.jitpack.io/#PingerOne/ImageGo) |[中文文档]()|[EnglishDocument]()

> 二次封装图片加载框架ImageGo。使用Kotlin语言编写，使用Androidx的API，支持随时替换底层加载框架Glide、Picasso和Fresco，支持加载带边框圆形、多方位圆角、高斯模糊等特效图片，支持使用进度条加载，GIF加载，保存图片到本地，获取图片缓存大小，清除图片缓存等多种功能。
>
> 这个库是公司的业务衍生出来的，项目一直都在使用，遇到的问题基本都修复了，大部分的图片加载模式应该是可以满足的。如果大家使用遇到什么问题，欢迎提交Issues。


## 添加依赖

1. 在项目根目录的build.gradle文件中添加jitpack仓库路径。

        allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
            }
        }

2. 在app的build.gradle文件中引入仓库依赖。

    * 如果需要自己定义加载策略，则添加以下依赖，然后实现ImageStrategy接口，重写加载图片的方法：

          implementation 'implementation 'com.github.PingerOne.ImageGo:imagego_core:2.0.5''

    * 如果使用Glide加载图片，则添加依赖：

          implementation 'implementation 'com.github.PingerOne.ImageGo:imagego_glide:2.0.5''

    * 如果使用Picasso加载图片，则添加依赖：

          implementation 'implementation 'com.github.PingerOne.ImageGo:imagego_picasso:2.0.5''


## 使用方法
1. 添加依赖后，在Application中设置图片加载策略。其中GlideImageStrategy也可以是自己定义的加载策略。

        ImageGo
          .setDebug(true)   // 开发模式
          .setStrategy(GlideImageStrategy())  // 图片加载策略
          .setDefaultBuilder(ImageOptions.Builder())  // 图片加载配置属性，可使用默认属性

2. 直接使用加载图片的API加载。

        loadImage(imageUrl imageView,listener = null,options = null)


## 官方API

#### 加载普通图片

    /**
    * 加载网络图片，支持渐变动画和GIF加载，可以配置加载监听，和其他Options配置项
    *
    * @param any 图片资源
    * @param view 展示的View
    * @param listener 监听加载对象
    * @param placeHolder 占位图资源id
    * @param options 图片加载配置项
    */
    fun loadImage(any: Any?, view: View?, listener: OnImageListener? = null, placeHolder: Int = 0, options: ImageOptions? = null)


#### 手动加载GIF图片


    /**
     * 手动加载GIF图片，使用[loadImage]方法可以自动加载GIF图

     * @param any 图片资源
     * @param view 展示的View
     * @param listener 监听加载对象
     */
    fun loadGif(any: Any?, view: View?, listener: OnImageListener? = null)


#### 加载Bitmap图片


    /**
     * 异步加载图片资源，在回调中输出Bitmap对象。可以在主线程直接调用
     *
     * @param context 上下文
     * @param any 图片资源
     * @param listener　加载图片的回调
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener)

    ---------------------------------------------------------------------------

    /**
     * 同步加载图片资源，生成Bitmap对象。必须在子线程调用，并且处理异常。
     *
     * @param context 上下文
     * @param any 图片资源
     * @return Bitmap对象，可能为null
     */
    fun loadBitmap(context: Context?, any: Any?): Bitmap?


#### 加载圆形图片

    /**
     * 加载圆形图片,支持设置边框大小和边框颜色，默认没有边框
     * @param any 图片资源
     * @param view　展示视图
     * @param borderWidth　边框的大小
     * @param borderColor　边框的颜色
     * @param listener　加载回调
     */
    fun loadCircle(any: Any?, view: View?, borderWidth: Int = 0, borderColor: Int = 0, listener: OnImageListener? = null)


#### 加载圆角图片

    /**
     * 加载圆角图片，默认圆角为4dp，四个角都是圆角。可以使用RoundType来控制圆角的位置。
     * @param any 图片链接
     * @param view 展示
     * @param roundRadius　圆角的角度
     * @param roundType　圆角图片的角位置
     * @param listener 加载回调
     */
    fun loadRound(any: Any?, view: View?, roundRadius: Int = 12, roundType: RoundType = RoundType.ALL, listener: OnImageListener? = null)


#### 加载高斯模糊图片

    /**
     * 加载高斯模糊图片,默认25个模糊点，1个模糊半径。
     * @param any　图片链接
     * @param view　展示
     * @param blurRadius　高斯模糊的度数
     * @param blurSampling　高斯模糊的半径
     * @param listener 回调
     */
    fun loadBlur(any: Any?, view: View?, blurRadius: Int = 25, blurSampling: Int = 1, listener: OnImageListener? = null)


#### 保存图片到本地

    /**
    * 保存网络图片到本地
    * @param context　上下文
    * @param any　保存的图片资源
    * @param path 图片保存的路径
    * @param listener　图片保存的回调
    */
    fun saveImage(context: Context?, any: Any?, path: String? = null, listener: OnImageSaveListener? = null)




## Author
Name：Pinger

Email：fungoit@gmail.com

## License
Apache 2.0，更多细节请查看[LICENSE](https://github.com/PingerOne/ImageGo/blob/master/LICENSE)。
