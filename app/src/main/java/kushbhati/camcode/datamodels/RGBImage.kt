package kushbhati.camcode.datamodels

data class RGBImage(
    val resolution: Resolution,
    val rChannel: ByteArray,
    val gChannel: ByteArray,
    val bChannel: ByteArray
)