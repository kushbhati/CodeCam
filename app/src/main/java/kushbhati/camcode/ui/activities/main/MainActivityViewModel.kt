package kushbhati.camcode.ui.activities.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.datamodels.YUVImage
import kushbhati.camcode.domain.AnalysisPoint
import kushbhati.camcode.domain.StreamAnalysis
import kushbhati.camcode.domain.CameraHandling
import kushbhati.camcode.domain.Signal
import kushbhati.camcode.domain.SignalPoint

class MainActivityViewModel : ViewModel() {

    private var cameraHandling: CameraHandling = CameraHandling()
    private var streamAnalysis: StreamAnalysis = StreamAnalysis()

    var previewFrame: MutableState<RGBImage?> = mutableStateOf(null)
    var frameRate = mutableIntStateOf(0)

    private var timeStamp: Long = 0

    private val analysisPoints = mutableListOf<AnalysisPoint>()
    val signalPoints = mutableListOf<SignalPoint>()
    val signals = mutableListOf<Signal>()

    // fixed sizes for graphs
    val viewableAPs = mutableStateOf(listOf(0f))
    val viewableSPs = mutableStateOf(listOf(0f))
    //val viewableSs =

    init {
        cameraHandling.setFrameReceiver {
            newFrame(it)
            previewFrame.value = it.toGreyImage().asRGBImage()
            frameRate.intValue = (1000000000L / (it.metadata.timeStamp - timeStamp)).toInt() // temp
            timeStamp = it.metadata.timeStamp
        }
    }

    private fun newFrame(frame: YUVImage) {
        Log.d("Tinfo NF", "${android.os.Process.myTid()}")
        val analysisPoint = streamAnalysis.frameToAP(frame)
        analysisPoints.add(analysisPoint)
        viewableAPs.value = analysisPoints.takeLast(90).map { it.value.toFloat() / 8000_0000 }
        val signalPoint = streamAnalysis.APToSP(analysisPoint)
        signalPoints.add(signalPoint)
        viewableSPs.value = signalPoints.takeLast(90).map { if (it.value) .9f else .1f }
    }

}