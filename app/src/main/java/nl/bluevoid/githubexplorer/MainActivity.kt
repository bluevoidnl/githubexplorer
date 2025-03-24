package nl.bluevoid.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import nl.bluevoid.githubexplorer.presentation.GitHubExplorerViewmodel
import nl.bluevoid.githubexplorer.presentation.NavigationEvent
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.GitHubDetailView
import nl.bluevoid.githubexplorer.presentation.ui.GitHubOverView
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: GitHubExplorerViewmodel by viewModel()

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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collectLatest(::navigate)
            }
        }
    }

    private fun navigate(destination: NavigationEvent) {
        when (destination) {
            is NavigationEvent.OpenInAppBrowser -> {
                CustomTabsIntent.Builder().build().launchUrl(this, destination.url)
            }
        }
    }
}