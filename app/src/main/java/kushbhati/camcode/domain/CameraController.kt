package kushbhati.camcode.domain

import kushbhati.camcode.datamodels.YUVImage

interface CameraController {

    interface FrameReceiver {
        fun onReceive(image: YUVImage)
    }

    fun startCamera()
    fun setFrameReceiver(frameReceiver: FrameReceiver)
}