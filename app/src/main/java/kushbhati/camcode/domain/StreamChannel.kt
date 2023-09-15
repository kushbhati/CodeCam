package kushbhati.camcode.domain

sealed class StreamChannel {
    object DIRECT : StreamChannel()
    object GREYSCALE : StreamChannel()
    object BLURRED : StreamChannel()
    object CLAMPED : StreamChannel()
    class REGIONAL(val region: Int) : StreamChannel()
}