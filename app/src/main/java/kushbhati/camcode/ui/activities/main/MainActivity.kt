package kushbhati.camcode.ui.activities.main

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kushbhati.camcode.domain.StreamState
import kushbhati.camcode.ui.activities.main.components.composables.CamPermissionReq
import kushbhati.camcode.ui.activities.main.components.composables.FreeGraph
import kushbhati.camcode.ui.activities.main.components.composables.PreviewScreen
import kushbhati.camcode.ui.activities.main.components.composables.ThresholdGraph
import kushbhati.camcode.ui.theme.CodeCamTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    private val cameraPermission = android.Manifest.permission.CAMERA

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (checkSelfPermission(cameraPermission)) {
            PackageManager.PERMISSION_GRANTED -> {}
            PackageManager.PERMISSION_DENIED -> {
                requestPermissions(arrayOf(cameraPermission), 0)
            }
        }

        setContent {
            CodeCamTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(title = {
                            Text(text = "Camera")
                        })
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = true,
                                onClick = { /*TODO*/ },
                                icon = { Icon(Icons.Default.Home, "") },)
                            NavigationBarItem(
                                selected = false,
                                onClick = { /*TODO*/ },
                                icon = { Icon(Icons.Default.ThumbUp, "") })
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 12.dp)
                    ) {
                        item {
                            PreviewScreen(
                                viewModel.previewFrame.value
                            ) {
                                if (viewModel.streamStatus.streamState == StreamState.CAMERA_ACCESS_DENIED) {
                                    CamPermissionReq(
                                        modifier = Modifier.align(Alignment.Center),
                                        onclick = {
                                            requestPermissions(
                                                arrayOf(android.Manifest.permission.CAMERA),
                                                0
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            FreeGraph(
                                viewModel.debugData,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(80.dp)
                            )
                            ThresholdGraph(
                                viewModel.debugData,
                                0.5f,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(80.dp)
                            )
                            FreeGraph(
                                viewModel.debugData,
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(80.dp)
                            )
                            Text(viewModel.frameRate.intValue.toString())
                        }
                    }
                }
            }
        }
    }
}