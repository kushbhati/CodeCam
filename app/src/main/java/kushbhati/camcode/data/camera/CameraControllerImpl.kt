package kushbhati.camcode.data.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.view.Surface
import kushbhati.camcode.datamodels.Metadata
import kushbhati.camcode.datamodels.YUVImage
import kushbhati.camcode.domain.CameraController
import java.util.concurrent.Executors
import kotlin.properties.Delegates


class CameraControllerImpl(context: Context) : CameraController {

    private val cameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val imageReader: ImageReader =
        ImageReader.newInstance(640, 480, ImageFormat.YUV_420_888, 10)
    private val surface: Surface =
        imageReader.surface

    private lateinit var cameraList: Array<String>
    private var currentCameraIndex: Int = 0
    private lateinit var device: CameraDevice
    private var naturalRotation: Int = 0

    private val executor = Executors.newFixedThreadPool(64)

    /*private val imageListener = object : Runnable {
        private val executor = Executors.newFixedThreadPool(64)
        private val frameReceiver: ((YUVImage) -> Unit)? = null
        private val onImageAvailableListener =
        
        override fun run() {
            TODO("Not yet implemented")
        }
    }*/

    init {
        obtainCameraList()
    }


    @Throws(CameraAccessException::class)
    private fun obtainCameraList() {
        cameraList = cameraManager.cameraIdList
        cameraList.filter { cameraId ->
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val outputFormats = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.outputFormats?: IntArray(0)
            ImageFormat.YUV_420_888 in outputFormats && ImageFormat.RAW_SENSOR in outputFormats
        }
    }


    private fun initCaptureSession() {
        val config = OutputConfiguration(
            surface
        )
        val sessionConfig = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            listOf(config),
            Executors.newSingleThreadExecutor(),
            object: CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    val captureRequest = session.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                    captureRequest.addTarget(surface)
                    session.setRepeatingRequest(captureRequest.build(), null, null)
                }
                override fun onConfigureFailed(session: CameraCaptureSession) { TODO() }
            }
        )
        device.createCaptureSession(sessionConfig)
    }


    override fun startCamera() {
        try {
            cameraManager.openCamera(cameraList[currentCameraIndex],
                Executors.newSingleThreadExecutor(),
                object : CameraDevice.StateCallback() {
                    override fun onOpened(cameraDevice: CameraDevice) {
                        device = cameraDevice
                        val characteristics = cameraManager.getCameraCharacteristics(cameraList[currentCameraIndex])
                        naturalRotation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
                        initCaptureSession()
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        TODO("Not yet implemented")
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        TODO("Not yet implemented")
                    }

                })
        } catch (_: SecurityException) {}
    }


    fun YUVImage.toNaturalRotation() {
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


    override fun setFrameReceiver(frameReceiver: CameraController.FrameReceiver) {
        imageReader.setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader?) {
                val image = reader?.acquireLatestImage() ?: return
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

                val yuvImage = YUVImage(
                    Metadata(width, height, timeStamp),
                    yChannel,
                    uChannel,
                    vChannel
                )

                yuvImage.toNaturalRotation()

                executor.submit { frameReceiver.onReceive(yuvImage) }
            }
        }, null)
    }
}