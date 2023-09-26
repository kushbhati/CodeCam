package kushbhati.imagelib

class ImageProcessorHelper {

    external fun ByteArray.blur(rowStride: Int, rowCount: Int): ByteArray


    external fun ByteArray.clamp(): ByteArray

    companion object {
        init {
            System.loadLibrary("imagelib")
        }
    }
}