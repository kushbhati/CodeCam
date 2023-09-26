package kushbhati.camcode.ui.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import kushbhati.camcode.ui.activities.main.components.composables.PreviewScreen
import kushbhati.camcode.ui.theme.CodeCamTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(this)

        setContent {
            CodeCamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreviewScreen(
                        viewModel.resolution.value,
                        viewModel.previewFrame.value
                    ) {
                        /*Box(modifier = Modifier
                            .size(100.dp)
                            .offset(
                                with(LocalDensity.current) { width.toDp() } * tracerX - 50.dp,
                                with(LocalDensity.current) { height.toDp() } * tracerY - 50.dp
                            )
                            .border(5.dp, Color.Red, CircleShape)
                        )*/
                    }
                }
            }
        }
    }
}