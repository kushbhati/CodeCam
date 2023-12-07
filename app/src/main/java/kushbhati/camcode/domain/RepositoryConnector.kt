package kushbhati.camcode.domain

import android.app.Application
import kushbhati.camcode.CamCode
import kushbhati.camcode.data.camera.CameraAdapter
import kushbhati.camcode.data.imageprocessing.IPAdaptor

object RepositoryConnector {
    fun defaultCameraController(): CameraController {
        return CameraAdapter.cameraController(CamCode.application)
    }

    fun defaultImageProcessor(): ImageProcessor {
        return IPAdaptor.imageProcessor
    }
}