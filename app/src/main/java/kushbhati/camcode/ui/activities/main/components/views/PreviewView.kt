package kushbhati.camcode.ui.activities.main.components.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.view.SurfaceHolder
import android.view.SurfaceView
import kushbhati.camcode.datamodels.RGBImage

@SuppressLint("ViewConstructor")
class PreviewView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var surfaceAvailable: Boolean = false

    init {
        holder.addCallback(this)
    }

    private fun RGBImage.toUnrotatedBitmap(): Bitmap {
        val pixelData = IntArray(resolution.width * resolution.height) {
            Color.rgb(
                rChannel[it].toUByte().toInt(),
                gChannel[it].toUByte().toInt(),
                bChannel[it].toUByte().toInt()
            )
        }
        return Bitmap.createBitmap(
            pixelData,
            resolution.width,
            resolution.height,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun RGBImage.computeRotationMatrix(): Matrix {
        val matrix = Matrix()
        matrix.setRotate(resolution.desiredRotation.toFloat())
        when (resolution.desiredRotation) {
            90 -> matrix.postTranslate(resolution.height.toFloat(), 0f)
            270 -> matrix.postTranslate(0f, resolution.width.toFloat())
            180 -> matrix.postTranslate(resolution.height.toFloat(), resolution.width.toFloat())
        }
        return matrix
    }

    fun render(rgbImage: RGBImage) {
        if (surfaceAvailable) {
            val bitmap = rgbImage.toUnrotatedBitmap()
            val matrix = rgbImage.computeRotationMatrix()
            val canvas = holder.lockHardwareCanvas()
            synchronized(holder) {
                canvas.drawBitmap(bitmap, matrix, null)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceAvailable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceAvailable = false
    }
}