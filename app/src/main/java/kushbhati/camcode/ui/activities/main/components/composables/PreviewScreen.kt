package kushbhati.camcode.ui.activities.main.components.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kushbhati.camcode.datamodels.RGBImage
import kushbhati.camcode.ui.activities.main.components.views.PreviewView

@Composable
fun PreviewScreen(
    previewFrame: RGBImage?,
    modifier: Modifier = Modifier,
    overlay: @Composable (BoxScope.(LayoutCoordinates?) -> Unit)? = null
) {
    val layoutCoordinates: MutableState<LayoutCoordinates?> = remember { mutableStateOf(null) }
    val previewView: MutableState<PreviewView?> = remember { mutableStateOf(null) }

    Box(modifier = modifier
        .onGloballyPositioned {
            layoutCoordinates.value = it
        }
        .clip(RoundedCornerShape(20.dp))
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


        overlay?.invoke(this, layoutCoordinates.value)
    }
}
