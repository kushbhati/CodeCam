package kushbhati.camcode.domain

import android.content.Context
import kushbhati.camcode.data.camera.CameraAdapter
import kushbhati.camcode.data.imageprocessing.IPAdaptor
import kushbhati.camcode.datamodels.GreyImage
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.datamodels.Resolution
import kushbhati.camcode.datamodels.YUVImage

class LogicHandler(context: Context, val frameDestination: (RGBImage) -> Unit) {
    private val cameraController: CameraController = CameraAdapter.cameraController(context)
    private val imageProcessor: ImageProcessor = IPAdaptor.imageProcessor

    var currentStreamChannel: StreamChannel = StreamChannel.DIRECT
    val resolution: Resolution = cameraController.getResolution()

    init {
        cameraController.startCamera()
        cameraController.setFrameReceiver(::cameraStream)
    }

    private fun cameraStream(yuvImage: YUVImage) {
        if (currentStreamChannel == StreamChannel.DIRECT) {
            frameDestination(yuvImage.toGreyImage().toRGBImage())
        }
        greyedStream(yuvImage.toGreyImage())
    }

    private fun greyedStream(greyImage: GreyImage) {
        if (currentStreamChannel == StreamChannel.GREYSCALE) {
            frameDestination(greyImage.toRGBImage())
        }
        //TODO blurredStream(imageProcessor.blur(greyImage))
    }

    private fun blurredStream(blurredImage: GreyImage) {
        if (currentStreamChannel == StreamChannel.BLURRED) {
            frameDestination(blurredImage.toRGBImage())
        }
        clampedStream(imageProcessor.clamp(blurredImage))
    }

    private fun clampedStream(clampedImage: GreyImage) {
        if (currentStreamChannel == StreamChannel.CLAMPED) {
            frameDestination(clampedImage.toRGBImage())
        }
        regionalStream(imageProcessor.quantify(clampedImage))
    }

    private fun regionalStream(localImage: List<GreyImage>) {
        if (currentStreamChannel is StreamChannel.REGIONAL) {
            frameDestination(localImage[
                (currentStreamChannel as? StreamChannel.REGIONAL)?.region ?: 0
            ].toRGBImage())
        }
        //imageProcessor.analyse(localImage) //TODO
    }

}