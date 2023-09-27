package kushbhati.camcode.datamodels

class YUVImage(
    val metadata: Metadata,
    val yMatrix: ByteArray,
    val uMatrix: ByteArray,
    val vMatrix: ByteArray
) {
    fun toGreyImage(): GreyImage {
        return GreyImage(
            metadata = metadata,
            pixelMatrix = yMatrix
        )
    }
}