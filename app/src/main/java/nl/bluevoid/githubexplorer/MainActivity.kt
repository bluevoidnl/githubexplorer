package nl.bluevoid.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                when (uiState) {
                    is UiState.Overview -> GitHubOverView(
                        uiState = uiState,
                        overviewScreenInteractor = viewModel
                    )

                    is UiState.Detail -> GitHubDetailView(uiState = uiState, interactor = viewModel)
                }
            }
        }
    }
}