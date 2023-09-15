package kushbhati.camcode.datamodels


@OptIn(ExperimentalUnsignedTypes::class)
class GreyImage (
    val resolution: Resolution,
    val pixelMatrix: UByteArray
) {
    fun toRGBImage(): RGBImage {
        return RGBImage(
            resolution = resolution,
            rChannel = pixelMatrix,
            gChannel = pixelMatrix,
            bChannel = pixelMatrix
        )
    }
}
