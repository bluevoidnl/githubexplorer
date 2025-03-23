package nl.bluevoid.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.bluevoid.githubexplorer.presentation.ExplorerViewmodel
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.GitHubDetailView
import nl.bluevoid.githubexplorer.presentation.ui.GitHubOverView
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ExplorerViewmodel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    GitHubView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        uiState
                    )
                }
            }
        }
    }
}

@Composable
fun GitHubView(modifier: Modifier = Modifier, uiState: UiState) {
    Box(
        modifier = modifier
    ) {
        when (uiState) {
            is UiState.Overview -> GitHubOverView(uiState = uiState)
            is UiState.Detail -> GitHubDetailView(uiState = uiState)
        }
    }
}