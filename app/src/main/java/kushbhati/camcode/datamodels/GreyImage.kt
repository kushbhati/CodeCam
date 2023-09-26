package kushbhati.camcode.datamodels

class GreyImage (
    val resolution: Resolution,
    val pixelMatrix: ByteArray
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
