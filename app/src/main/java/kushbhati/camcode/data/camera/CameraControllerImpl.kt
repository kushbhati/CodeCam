package kushbhati.camcode.data.camera

import android.app.Application
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import kushbhati.camcode.datamodels.Metadata
import kushbhati.camcode.datamodels.YUVImage
import kushbhati.camcode.domain.CameraController
import java.util.concurrent.Executors


class CameraControllerImpl(
    application: Application
) : CameraController {
    private val cameraManager = application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private lateinit var cameraList: Array<String>

    private lateinit var cameraDevice: CameraDevice

    private var naturalRotation: Int = 0
    private var frameDuration: Long = 0L
    private var imageFormat: Int = ImageFormat.YUV_420_888
    private var imageSize: Size = Size(640, 480)

    private lateinit var imageReader: ImageReader

    private lateinit var sessionConfiguration: SessionConfiguration

    private val executor = Executors.newFixedThreadPool(64)

    private val cameraHandler = object : CameraDevice.StateCallback() {
        override fun onOpened(device: CameraDevice) {
            cameraDevice = device
            obtainCameraCharacteristics()
            initSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
        }

    }

    private var frameReceiver: CameraController.FrameReceiver? = null

    init {
        enumerateCameras()
        openCandidateCamera()
    }


    @Throws(CameraAccessException::class)
    private fun enumerateCameras() {
        cameraList = cameraManager.cameraIdList
        cameraList.filter { cameraId ->
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val outputFormats = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.outputFormats?: IntArray(0)
            imageFormat in outputFormats
        }
    }


    @Throws(CameraAccessException::class, SecurityException::class)
    private fun openCandidateCamera() {
        cameraManager.openCamera(
            cameraList.first(),
            Executors.newSingleThreadExecutor(),
            cameraHandler
        )
    }


    @Throws(CameraAccessException::class)
    private fun obtainCameraCharacteristics() {
        val characteristics = cameraManager.getCameraCharacteristics(cameraList.first())
        val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        imageSize = streamConfigurationMap?.
            getOutputSizes(imageFormat)?.
            filter { it.width >= 640 }?.
            minBy { it.height * it.width } ?: imageSize
        frameDuration = streamConfigurationMap?.getOutputMinFrameDuration(imageFormat, imageSize) ?: 0
        naturalRotation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
        Log.d("Values", "$imageSize $frameDuration $naturalRotation")
    }


    @Throws(CameraAccessException::class)
    private fun initSession() {
        imageReader = ImageReader.newInstance(
            imageSize.width,
            imageSize.height,
            imageFormat,
            16
        )

        imageReader.setOnImageAvailableListener(
            { reader ->
                val image = reader?.acquireLatestImage() ?: return@setOnImageAvailableListener
                val width = image.width
                val height = image.height
                val timeStamp = image.timestamp
                val yChannel = ByteArray(height * width)
                val uChannel = ByteArray(height/2 * width/2)
                val vChannel = ByteArray(height/2 * width/2)

                image.planes[0].buffer.get(yChannel)
                image.planes[1].buffer.get(uChannel)
                image.planes[2].buffer.get(vChannel)
                image.close()

                executor.submit {
                    val yuvImage = YUVImage(
                        Metadata(width, height, timeStamp),
                        yChannel,
                        uChannel,
                        vChannel
                    )
                    yuvImage.toNaturalRotation()
                    frameReceiver?.onReceive(yuvImage)
                }
            },
            Handler(Looper.getMainLooper())
        )

        val sessionCallback = object: CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val captureRequest = session.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                captureRequest.addTarget(imageReader.surface)
                captureRequest.set(CaptureRequest.SENSOR_FRAME_DURATION, frameDuration)
                session.setRepeatingRequest(captureRequest.build(), null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                TODO()
            }
        }

        sessionConfiguration = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            listOf(OutputConfiguration(imageReader.surface)),
            Executors.newSingleThreadExecutor(),
            sessionCallback
        )

        cameraDevice.createCaptureSession(sessionConfiguration)
    }


    override fun openCamera() {
        openCandidateCamera()
    }

    private fun YUVImage.toNaturalRotation() {
        when (naturalRotation) {
            90 -> {
                val yCopy = yMatrix.clone()
                val uCopy = uMatrix.clone()
                val vCopy = vMatrix.clone()
                for (r in 0 until metadata.height) for (c in 0 until metadata.width) {
                    yMatrix[metadata.height * c + metadata.height - 1 - r] =
                        yCopy[metadata.width * r + c]
                }
                for (r in 0 until metadata.height/2) for (c in 0 until metadata.width/2) {
                    uMatrix[metadata.height/2 * c + metadata.height/2 - 1 - r] =
                        uCopy[metadata.width/2 * r + c]
                    vMatrix[metadata.height/2 * c + metadata.height/2 - 1 - r] =
                        vCopy[metadata.width/2 * r + c]
                }
                run {
                    val temp = metadata.height
                    metadata.height = metadata.width
                    metadata.width = temp
                }
            }

            270 -> {
                TODO()
            }

            180 -> {
                TODO()
            }
        }
    }


    override fun setFrameReceiver(frameReceiver: CameraController.FrameReceiver?) {
        this.frameReceiver = frameReceiver
    }
}