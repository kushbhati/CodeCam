package kushbhati.camcode.data.imageprocessing

import kushbhati.camcode.datamodels.GreyImage
import kushbhati.camcode.domain.ImageProcessor
import kushbhati.imagelib.ImageProcessorHelper

@OptIn(ExperimentalUnsignedTypes::class)
object ImageProcessorImpl : ImageProcessor {
    private val imageProcessorHelper = ImageProcessorHelper()

    /*
    fun analyzer {
            val alpha = imageProxy.planes[0]
            //Log.d("alpha", alpha.rowStride.toString())
            var max: Int = 0
            var max_x: Int = 0
            var max_y: Int = 0
            val lumBuffer = UByteArray(imageProxy.width * imageProxy.height) {alpha.buffer.get(it).toUByte()}
            for (i in 0 until (imageProxy.width * imageProxy.height))
            {
                if (lumBuffer[i].toInt() > max) {
                    max = lumBuffer[i].toInt()
                    max_y = i % alpha.rowStride
                    max_x = i / alpha.rowStride
                }
            }
            //Log.d("fd", alpha.rowStride.toString())
            //Log.d("bright", Pair(max_x, max_y).toString())
            //Log.d("bright", Pair(imageProxy.width, imageProxy.height).toString())
            sq_x = 1 - (max_x.toFloat() / imageProxy.height)
            sq_y = max_y.toFloat() / imageProxy.width
            imageProxy.setCropRect(Rect(0, 0, -200, -200))
            //Log.d("crop_rect", imageProxy.cropRect.toString())
            imageProxy.close()
        }
        */
    override fun blur(image: GreyImage): GreyImage {
        val newImage = GreyImage(
            image.metadata,
            with(imageProcessorHelper) {
                image.pixelMatrix.blur(image.metadata.width, image.metadata.height)
            }
        )
        return newImage
    }

    override fun clamp(image: GreyImage): GreyImage {
        val newImage = GreyImage(
            image.metadata,
            with(imageProcessorHelper) { image.pixelMatrix.clamp() }
        )
        return newImage
    }

    override fun quantify(greyImage: GreyImage): List<GreyImage> {
        TODO("Not yet implemented")
    }

    override fun analyse(quantifiedStreams: List<GreyImage>) {
        TODO("Not yet implemented")
    }
}