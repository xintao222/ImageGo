
# ImageGo(Second Picture Loading Framework)
[![](https://www.jitpack.io/v/PingerOne/ImageGo.svg)](https://www.jitpack.io/#PingerOne/ImageGo) |[中文文档](https://github.com/PingerOne/ImageGo/blob/master/README_ZH.md)|[EnglishDocument](https://github.com/PingerOne/ImageGo/blob/master/README.md)

Secondary encapsulation image loading framework ImageGo. Written in Kotlin language, using Androidx API, support to replace the underlying loading framework Glide, Picasso and Fresco at any time, support loading special effects images with border round, multi-directional rounded corners, Gaussian blur, support for progress bar loading, GIF loading, saving Images to the local, get the image cache size, clear the image cache and other functions.

This library is derived from the company's business. The project has been used all the time, and the problems encountered have been basically fixed. Most of the image loading modes should be satisfactory. If you have any problems with your use, please feel free to submit Issues.

## Add dependency

1. Add the jitpack repository path to the build.gradle file in the project root directory.

        allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
            }
        }

2. Add the repository dependencies in the app's build.gradle file.

    * if you need to define your own loading strategy, add the following dependencies, then implement the ImageStrategy interface and override the method of loading the image：

          implementation 'com.github.PingerOne.ImageGo:imagego_core:2.0.5'

    * if you use Glide to load the image：

          implementation 'com.github.PingerOne.ImageGo:imagego_glide:2.0.5'

    * if you use Picasso to load the image：

          implementation 'com.github.PingerOne.ImageGo:imagego_picasso:2.0.5'


## How To Use
1. After adding dependencies, set the image loading policy in the Application. GlideImageStrategy can also be a self-defined loading strategy.

        ImageGo
          .setDebug(true)   // debug
          .setStrategy(GlideImageStrategy())  // Image loading strategy
          .setDefaultBuilder(ImageOptions.Builder())  // Image loading configuration properties, using default properties

2. Load directly using the API that loads the image.

        loadImage(imageUrl imageView,listener = null,options = null)


## Official API

#### Load normal picture

    /**
    * Load web images, support gradient animation and GIF loading, configure load monitoring, and other Options configuration items.
    *
    * @param any Image resource
    * @param view Displayed View
    * @param listener Listening load object
    * @param placeHolder Placeholder resource id
    * @param options Image loading configuration options
    */
    fun loadImage(any: Any?, view: View?, listener: OnImageListener? = null, placeHolder: Int = 0, options: ImageOptions? = null)


#### Manually load GIF images


    /**
     * Manually load GIF images, use the [loadImage] method to automatically load GIF images.
     *
     * @param any Image resource
     * @param view Displayed View
     * @param listener Listening load object
     */
    fun loadGif(any: Any?, view: View?, listener: OnImageListener? = null)


#### Load Bitmap image


    /**
     * Load the image resource asynchronously and output the Bitmap object in the callback. Can be called directly in the main thread.
     *
     * @param context
     * @param any Image resource
     * @param listener Listening load object
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener)

    ---------------------------------------------------------------------------

    /**
     * Synchronize the image resource to generate a Bitmap object. Must be called in a child thread and handle exceptions.
     *
     * @param context
     * @param any Image resource
     * @return Bitmap object，maybe null
     */
    fun loadBitmap(context: Context?, any: Any?): Bitmap?


#### Load a circular image

    /**
     * Load circular image, support setting border size and border color, no border by default.
     * @param any Image resource
     * @param view　Displayed View
     * @param borderWidth　Border size
     * @param borderColor　Border color
     * @param listener Listening load object
     */
    fun loadCircle(any: Any?, view: View?, borderWidth: Int = 0, borderColor: Int = 0, listener: OnImageListener? = null)


#### Load rounded corners image

    /**
     * Load the fillet image, the default fillet is 4dp, and the four corners are rounded. RoundType can be used to control the position of the fillet.
     * @param any Image resource
     * @param view Displayed View
     * @param roundRadius　Angle of fillet
     * @param roundType　Corner position of the rounded picture
     * @param listener Listening load object
     */
    fun loadRound(any: Any?, view: View?, roundRadius: Int = 12, roundType: RoundType = RoundType.ALL, listener: OnImageListener? = null)


#### Load Gaussian Blur Image

    /**
     * Load Gaussian blur pictures, default 25 blur points, 1 blur radius.
     * @param any Image resource
     * @param view　Displayed View
     * @param blurRadius　Gaussian fuzzy degree
     * @param blurSampling　Gaussian blur radius
     * @param listener Listening load object
     */
    fun loadBlur(any: Any?, view: View?, blurRadius: Int = 25, blurSampling: Int = 1, listener: OnImageListener? = null)


#### Save image to local

    /**
    * Save network image to local.
    * @param context　
    * @param any Image resource
    * @param path Path to save the image
    * @param listener Listening save object
    */
    fun saveImage(context: Context?, any: Any?, path: String? = null, listener: OnImageSaveListener? = null)




## Author
Name：Pinger

Email：fungoit@gmail.com

## License
Apache 2.0,See the [LICENSE](https://github.com/PingerOne/ImageGo/blob/master/LICENSE) file for details.
