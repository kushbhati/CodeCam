package kushbhati.camcode.domain

data class StreamStatus(
    var streamState: StreamState,
    var previewChannel: StreamChannel,
    var analysisChannel: StreamChannel
)