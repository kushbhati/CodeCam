package kushbhati.camcode.domain

import android.content.Context
import android.hardware.camera2.CameraManager
import kushbhati.camcode.CamCode
import kushbhati.camcode.data.camera.CameraControllerImpl
import kushbhati.camcode.data.imageprocessing.ImageProcessorImpl

object DependencyConnector {
    fun defaultCameraController(): CameraController {
        return CameraControllerImpl(
            CamCode.application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        )
    }

//    fun defaultImageProcessor(): ImageProcessor {
//        return ImageProcessorImpl
//    }
}