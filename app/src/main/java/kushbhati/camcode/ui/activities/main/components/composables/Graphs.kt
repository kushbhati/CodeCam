package kushbhati.camcode.ui.activities.main.components.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.dp

@Composable
fun FreeGraph(data: List<Float>, drawColor: Color, backgroundColor: Color, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
    ) {
        val height = size.height
        val width = size.width

        drawPoints(
            points = data.mapIndexed { index, fl ->
                Offset(index.toFloat() * width / (data.size-1) * .85f, (1 - fl) * height)
            },
            pointMode = PointMode.Polygon,
            color = drawColor,
            strokeWidth = 3.dp.toPx()
        )
    }
}

@Composable
fun ThresholdGraph(data: List<Float>, threshold: Float, drawColor: Color, backgroundColor: Color, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
    ) {
        val height = size.height
        val width = size.width

        drawPoints(
            points = data.mapIndexed { index, fl ->
                Offset(
                    index.toFloat() * width / (data.size-1) * .85f,
                    (if (fl>=threshold) 0.2f else 0.8f) * height)
            },
            pointMode = PointMode.Polygon,
            color = drawColor,
            strokeWidth = 3.dp.toPx()
        )
    }
}