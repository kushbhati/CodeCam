package kushbhati.camcode.domain

import kushbhati.camcode.datamodels.YUVImage

class CameraHandling {
    private var state: StreamState = StreamState.STREAM_UNINITIALISED

    private val cameraController: CameraController = DependencyConnector.defaultCameraController()

    fun setFrameReceiver(frameReceiver: (YUVImage) -> Unit) {
        state = try {
            cameraController.setFrameReceiver(
                object : CameraController.FrameReceiver {
                    override fun onReceive(image: YUVImage) = frameReceiver(image)
                }
            )
            StreamState.STREAM_ACTIVE
        } catch (_: SecurityException) {
            StreamState.CAMERA_ACCESS_DENIED
        }
    }
}