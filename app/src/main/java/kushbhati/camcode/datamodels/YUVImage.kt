package kushbhati.camcode.datamodels

import kushbhati.camcode.datamodels.GreyImage
import kushbhati.camcode.datamodels.Resolution


@OptIn(ExperimentalUnsignedTypes::class)
data class YUVImage(
    val resolution: Resolution,
    val yMatrix: UByteArray,
    val uMatrix: UByteArray,
    val vMatrix: UByteArray
) {
    fun toGreyImage(): GreyImage {
        return GreyImage(
            resolution = resolution,
            pixelMatrix = yMatrix
        )
    }
}