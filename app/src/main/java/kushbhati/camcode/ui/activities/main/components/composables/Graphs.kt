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

        /*val smoothDivisions = 1
        val smoothRatio = (smoothDivisions + 1) * 2
        val smoothData = MutableList(smoothRatio * (data.size - 1) + 1) {0f}
        for (i in 0 until data.size-1) {
            val factor = 1f / (smoothRatio)
            val start = data[i]
            val end = data[i + 1]
            val mid = (start + end) / 2
            val diff = end - start
            for (j in 0 until smoothRatio) {
                val exfat = if (j<smoothRatio)
                smoothData[smoothRatio * i + j] = start + diff * factor * j
            }
        }

        smoothData[smoothData.lastIndex] = data.last()

        val dataSize = smoothData.size - 1*/

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