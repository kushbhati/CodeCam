package kushbhati.camcode.domain

import android.util.Log
import kushbhati.camcode.datamodels.YUVImage

class StreamAnalysis {
//    private var localMax: UInt = 0u
//    private fun clamp(value: UInt): Double {
//        if (value > localMax) localMax = value
//        return value.toDouble() / localMax.toDouble()
//    }

    private val threshold = 6000_0000L

    fun frameToAP(image: YUVImage): AnalysisPoint {
        var value = 0L
        for (i in image.yMatrix) value += i.toLong() and 0xff
        Log.d("value: ", "$value")
        return AnalysisPoint(
            timeStamp = image.metadata.timeStamp,
            value = value
        )
    }

    fun APToSP(ap: AnalysisPoint): SignalPoint {
        val value = ap.value > threshold
        return SignalPoint(
            timeStamp = ap.timeStamp,
            value = value
        )
    }
}