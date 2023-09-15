package kushbhati.camcode.datamodels

import kushbhati.camcode.datamodels.Resolution

@OptIn(ExperimentalUnsignedTypes::class)
data class RGBImage(
    val resolution: Resolution,
    val rChannel: UByteArray,
    val gChannel: UByteArray,
    val bChannel: UByteArray
)