package kushbhati.camcode.datamodels

class GreyImage (
    val metadata: Metadata,
    val pixelMatrix: ByteArray
) {
    fun asRGBImage(): RGBImage {
        return RGBImage(
            metadata = metadata,
            rChannel = pixelMatrix,
            gChannel = pixelMatrix,
            bChannel = pixelMatrix
        )
    }
}
