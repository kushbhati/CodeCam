package kushbhati.camcode.datamodels

data class RGBImage(
    val metadata: Metadata,
    val rChannel: ByteArray,
    val gChannel: ByteArray,
    val bChannel: ByteArray
)