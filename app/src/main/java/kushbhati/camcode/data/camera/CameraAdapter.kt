package kushbhati.camcode.data.camera

import android.app.Application
import android.hardware.camera2.CameraManager
import kushbhati.camcode.domain.CameraController

object CameraAdapter {
    fun cameraController(cameraManager: Application): CameraController = CameraControllerImpl(cameraManager)
}