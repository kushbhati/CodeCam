package kushbhati.camcode.datamodels

import kushbhati.camcode.datamodels.GreyImage
import kushbhati.camcode.datamodels.Resolution

class YUVImage(
    val resolution: Resolution,
    val yMatrix: ByteArray,
    val uMatrix: ByteArray,
    val vMatrix: ByteArray
) {
    fun toGreyImage(): GreyImage {
        return GreyImage(
            resolution = resolution,
            pixelMatrix = yMatrix
        )
    }
}