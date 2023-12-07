package kushbhati.camcode.domain

import android.content.Context
import androidx.annotation.RequiresPermission
import kushbhati.camcode.datamodels.YUVImage

interface CameraController {

    interface FrameReceiver {
        fun onReceive(image: YUVImage)
    }

    fun openCamera()

    fun setFrameReceiver(frameReceiver: FrameReceiver?)
}