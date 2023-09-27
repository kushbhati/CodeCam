package kushbhati.camcode.ui.activities.main.components.composables

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
import kushbhati.camcode.ui.activities.main.components.views.PreviewView

@Composable
fun PreviewScreen(
    previewFrame: RGBImage?,
    overlay: @Composable ((LayoutCoordinates?) -> Unit)? = null
) {
    val layoutCoordinates: MutableState<LayoutCoordinates?> = remember { mutableStateOf(null) }
    val previewView: MutableState<PreviewView?> = remember { mutableStateOf(null) }

    Box(modifier = Modifier
        .onGloballyPositioned {
            layoutCoordinates.value = it
        }
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(previewView.value?.getAspectRatio() ?: 1f),
            factory = { context ->
                PreviewView(context).apply {
                    clipToOutline = true
                    previewView.value = this
                }
            },
            update = {
                if ((previewFrame != null)) {
                    it.render(previewFrame)
                }
            }
        )

        overlay?.invoke(layoutCoordinates.value)
    }
}
