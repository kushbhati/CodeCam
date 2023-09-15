package kushbhati.camcode.data.camera

import android.content.Context
import kushbhati.camcode.domain.CameraController

object CameraAdapter {
    fun cameraController(context: Context): CameraController = CameraControllerImpl(context)
}