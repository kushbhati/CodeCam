package kushbhati.camcode.domain

import android.content.Context
import kushbhati.camcode.datamodels.GreyImage
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.datamodels.YUVImage

class LogicHandler(
    private val streamStatus: StreamStatus,
    val frameDestination: (RGBImage) -> Unit
) {
    private val cameraController: CameraController = RepositoryConnector.defaultCameraController()
    private val imageProcessor: ImageProcessor = RepositoryConnector.defaultImageProcessor()

    init {
        try {
            cameraController.setFrameReceiver(
                object : CameraController.FrameReceiver {
                    override fun onReceive(image: YUVImage) = cameraStream(image)
                }
            )
            streamStatus.streamState = StreamState.STREAM_ACTIVE
        }
        catch (_: SecurityException) {
            streamStatus.streamState = StreamState.CAMERA_ACCESS_DENIED
        }
    }

    private fun cameraStream(yuvImage: YUVImage) {
        if (streamStatus.streamChannel == StreamChannel.DIRECT) {
            frameDestination(yuvImage.toGreyImage().toRGBImage())
        }
        greyedStream(yuvImage.toGreyImage())
    }

    private fun greyedStream(greyImage: GreyImage) {
        if (streamStatus.streamChannel == StreamChannel.GREYSCALE) {
            frameDestination(greyImage.toRGBImage())
        }
        //blurredStream(imageProcessor.blur(greyImage))
    }

    private fun blurredStream(blurredImage: GreyImage) {
        if (streamStatus.streamChannel == StreamChannel.BLURRED) {
            frameDestination(blurredImage.toRGBImage())
        }
        clampedStream(imageProcessor.clamp(blurredImage))
    }

    private fun clampedStream(clampedImage: GreyImage) {
        if (streamStatus.streamChannel == StreamChannel.CLAMPED) {
            frameDestination(clampedImage.toRGBImage())
        }
        //regionalStream(imageProcessor.quantify(clampedImage))
    }

    /*private fun regionalStream(localImage: List<Pair<Pair<Int, Int>, GreyImage>>) {
        if (streamStatus.streamChannel is StreamChannel.REGIONAL) {
            frameDestination(localImage[
                (currentStreamChannel as? StreamChannel.REGIONAL)?.region ?: 0
            ].toRGBImage())
        }
        //imageProcessor.analyse(localImage) //TODO
    }*/

}