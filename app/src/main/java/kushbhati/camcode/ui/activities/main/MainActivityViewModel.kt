package kushbhati.camcode.ui.activities.main

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kushbhati.camcode.domain.LogicHandler
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.datamodels.Resolution
import kushbhati.camcode.domain.StreamChannel

class MainActivityViewModel : ViewModel() {

    private lateinit var logicHandler: LogicHandler

    var resolution: MutableState<Resolution> = mutableStateOf(Resolution(640, 480, 0))
    var previewFrame: MutableState<RGBImage?> = mutableStateOf(null)

    fun init(context: Context) {
        logicHandler = LogicHandler(context) { previewFrame.value = it }
        logicHandler.currentStreamChannel = StreamChannel.CLAMPED
        resolution.value = logicHandler.resolution
    }
}