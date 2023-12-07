package kushbhati.camcode.ui.activities.main.components.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kushbhati.camcode.datamodels.RGBImage

@SuppressLint("ViewConstructor")
class PreviewView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var surfaceAvailable: Boolean = false
    private var sHeight: Int = 640
    private var sWidth: Int = 480

    init {
        holder.addCallback(this)
        holder.setFixedSize(sWidth, sHeight)
    }

    fun getAspectRatio(): Float {
        return sWidth.toFloat() / sHeight
    }

    private fun RGBImage.toBitmap(): Bitmap {
        val pixelData = IntArray(metadata.width * metadata.height) {
            Color.rgb(
                rChannel[it].toUByte().toInt(),
                gChannel[it].toUByte().toInt(),
                bChannel[it].toUByte().toInt()
            )
        }
        return Bitmap.createBitmap(
            pixelData,
            metadata.width,
            metadata.height,
            Bitmap.Config.ARGB_8888
        )
    }

    fun render(image: RGBImage) {
        if (image.metadata.width != sWidth || image.metadata.height != sHeight)
        {
            sWidth = image.metadata.width
            sHeight = image.metadata.height
            holder.setFixedSize(sWidth, sHeight)
            Log.d("xyz", "sfs")
        }

        if (surfaceAvailable) {
            val bitmap = image.toBitmap()
            val canvas = holder.lockHardwareCanvas()
            synchronized(holder) { canvas.drawBitmap(bitmap, Matrix(), null) }
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