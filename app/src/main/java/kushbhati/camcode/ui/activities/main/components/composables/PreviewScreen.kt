package kushbhati.camcode.ui.activities.main.components.composables

import android.util.Log
import android.util.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.viewinterop.AndroidView
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.datamodels.Resolution
import kushbhati.camcode.ui.activities.main.components.views.PreviewView

@Composable
fun PreviewScreen(
    resolution: Resolution,
    previewFrame: RGBImage?,
    overlay: @Composable (LayoutCoordinates?) -> Unit
) {
    val size = with (resolution) {
        if (desiredRotation == 0 || desiredRotation == 180) Size(width, height)
        else Size(height, width)
    }
    val aspectRatio = size.width.toFloat() / size.height
    val layoutCoordinates: MutableState<LayoutCoordinates?> = remember { mutableStateOf(null) }

    Box(modifier = Modifier
        .onGloballyPositioned {
            layoutCoordinates.value = it
        }
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            factory = { context ->
                Log.d("init", "now")
                PreviewView(context).apply {
                    clipToOutline = true
                }
            },
            update = {
                it.holder.setFixedSize(size.width, size.height)
                if ((previewFrame != null)) {
                    Log.d("update", "now")
                    it.render(previewFrame)
                }
            }
        )

        overlay(layoutCoordinates.value)
    }
}
