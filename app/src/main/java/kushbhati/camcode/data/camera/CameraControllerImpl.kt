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
import android.os.Handler
import android.os.Looper
import android.view.Surface
import kushbhati.camcode.domain.CameraController
import kushbhati.camcode.datamodels.Resolution
import kushbhati.camcode.datamodels.YUVImage
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

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

    private val threadFactory = object : ThreadFactory {
        inner class LoopingThread : Thread() {
            init {
                Looper.prepare()
                Looper.loop()
            }
        }

        override fun newThread(r: Runnable?): Thread {
            return LoopingThread()
        }
    }

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


    override fun getResolution(): Resolution {
        val characteristics = cameraManager.getCameraCharacteristics(cameraList[currentCameraIndex])
        val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val outputSizes = streamConfigurationMap?.getOutputSizes(ImageFormat.YUV_420_888) ?: throw Exception()
        outputSizes.filter { it.width % 2 == 0 && it.height % 2 == 0}
        val outputAreas = outputSizes.map { listOf(it.width * it.height, it.width, it.height) }.sortedBy { it[0] }
        return with (outputAreas.first { it[0] >= 307200 }) { Resolution(this[1], this[2], getRotationDelta()) }
    }


    private fun getRotationDelta(): Int {
        val characteristics = cameraManager.getCameraCharacteristics(cameraList[currentCameraIndex])
        return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
    }


    override fun setFrameReceiver(frameReceiver: (YUVImage) -> Unit) {
        imageReader.setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader?) {
                val image = reader?.acquireLatestImage() ?: return
                val width = image.width
                val height = image.height
                val yChannel = ByteArray(height * width)
                val uChannel = ByteArray(image.height/2 * image.width/2)
                val vChannel = ByteArray(image.height/2 * image.width/2)
                image.planes[0].buffer.get(yChannel)
                image.planes[1].buffer.get(uChannel)
                image.planes[2].buffer.get(vChannel)
                image.close()
                val yuvImage = YUVImage(
                    Resolution(width, height, getRotationDelta()),
                    yChannel,
                    uChannel,
                    vChannel
                )
                executor.submit {
                    frameReceiver(yuvImage)
                }
            }
        }, null)
    }
}