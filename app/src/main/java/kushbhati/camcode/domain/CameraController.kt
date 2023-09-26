package kushbhati.camcode.domain

import kushbhati.camcode.datamodels.Resolution
import kushbhati.camcode.datamodels.YUVImage

interface CameraController {
    fun startCamera()
    fun getResolution(): Resolution
    fun setFrameReceiver(frameReceiver: (YUVImage) -> Unit)
}