package kushbhati.camcode.ui.activities.main

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.domain.LogicHandler
import kushbhati.camcode.domain.StreamChannel

class MainActivityViewModel : ViewModel() {

    private lateinit var logicHandler: LogicHandler

    var previewFrame: MutableState<RGBImage?> = mutableStateOf(null)
    var frameRate = mutableIntStateOf(0)

    private var timeStamp: Long = 0

    fun init(context: Context) {
        logicHandler = LogicHandler(context) {
            if (it.metadata.timeStamp > timeStamp) {
                previewFrame.value = it
                frameRate.intValue = (1000000000L / (it.metadata.timeStamp - timeStamp)).toInt()
                timeStamp = it.metadata.timeStamp
            }
        }
        logicHandler.currentStreamChannel = StreamChannel.CLAMPED
    }
}