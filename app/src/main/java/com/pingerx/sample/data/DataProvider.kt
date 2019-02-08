package com.pingerx.sample.data

import java.util.*

/**
 * @author Pinger
 * @since 18-11-28 下午3:08
 */
object DataProvider {

    private val random = Random()

    private val images = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399012596&di=c55d677c7892acc6032537ae2003b640&imgtype=0&src=http%3A%2F%2Fd.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F503d269759ee3d6d46f4a68e4e166d224f4ade40.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399012596&di=b038f45cbe26fa14e92d4a8bf64d859a&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F3bf33a87e950352aba22c6495e43fbf2b2118b34.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399145186&di=36ff9dcabffc46102c094ef657ad7b68&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F83025aafa40f4bfb0786420f0e4f78f0f7361813.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399158450&di=bfcd74ca96682b54c038b2b13aba6d05&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd058ccbf6c81800a447afd4abc3533fa838b47d2.jpg",

            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543993933&di=289a3cff759145d6358c167f5236b32e&imgtype=jpg&er=1&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F00e93901213fb80e93ebd89b3cd12f2eb9389440.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399213809&di=5f78c1a51f46468d52432c048c568f6e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd833c895d143ad4b86adf88c88025aafa50f06fe.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399247260&di=00ae7a27268616b84b74493fb04f0ed0&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fe61190ef76c6a7effb93d760f0faaf51f3de663a.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399247259&di=4f0de04cd71e5c81230e3f85a14fcdeb&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F50da81cb39dbb6fdad9a1f650424ab18962b37b4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399263411&di=7f2f755c233517a1f92e56b15eb5f4bc&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn08%2F68%2Fw2048h1220%2F20180720%2Fbe17-hfnsvzc2206543.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399213809&di=fe376507c07121bb95b3c5e4b2345dbd&imgtype=0&src=http%3A%2F%2Fd.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F500fd9f9d72a60599efa5f562234349b023bba90.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399282180&di=4ca09903eb24b2872401de9a48e962b7&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F09fa513d269759eee5b61ac2befb43166c22dfd1.jpg"
    )

    private val gifs = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399409638&di=aea710c5ea0a554ba320365fbb5f7b25&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201603%2F08%2F20160308174903_X2Vnc.gif",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399409638&di=a26656062ad631bedb1fbe4017ceaeca&imgtype=0&src=http%3A%2F%2Fs9.sinaimg.cn%2Fmw690%2F003yiy4jzy7ee2p2yYEf8%26690",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399409638&di=1f968c9b8e4d0070aee652c25b24a91a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160820%2Fd2e412453e5b43948c5c835a6ecc13f2_th.gif",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399409637&di=b90292e81c2394e305cee9847e308655&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160525%2F5e37f3cb80934797bd7a0cb7890f8447_th.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543399476824&di=d3dcbb3dd65fc9a33a26babf653cec73&imgtype=0&src=http%3A%2F%2Fs10.sinaimg.cn%2Fmw690%2F003yiy4jzy7ee27mYaZ99%26690",
            "http://img.soogif.com/woea52pEjn6yHy4HyFktlyfU3XKCMHQJ.gif",
            "http://img.soogif.com/RCqMoizxYZWnUJVBEcPhbsfM2gLJ2piL.gif",
            "https://n.sinaimg.cn/tech/transform/485/w292h193/20181127/1XpL-hpfyces5901720.gif"
    )


    fun getImageUrl(): String {
        return images[random.nextInt(images.size)]
    }

    fun getGifUrl(): String {
        return gifs[random.nextInt(gifs.size)]
    }

    fun getImageUrls(): List<String> {
        return images
    }

    fun getGifUrls(): List<String> {
        return gifs
    }
}