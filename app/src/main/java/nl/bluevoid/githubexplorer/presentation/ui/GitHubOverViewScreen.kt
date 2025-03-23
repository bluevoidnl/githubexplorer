package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.presentation.OverviewScreenInteractor
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubOverView(
    modifier: Modifier = Modifier,
    uiState: UiState.Overview,
    overviewScreenInteractor: OverviewScreenInteractor
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("ANB AMRO Github Explorer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (uiState) {
                is UiState.Overview.OverviewLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                is UiState.Overview.OverviewItems -> GitHubListView(modifier, uiState, overviewScreenInteractor)
                is UiState.Overview.OverviewLoadingError ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Something went wrong,\n please try again")
                            Button(onClick = { overviewScreenInteractor.onRetryLoading() }) {
                                Text("Retry")
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun GitHubListView(
    modifier: Modifier = Modifier, uiState: UiState.Overview.OverviewItems,
    overviewScreenInteractor: OverviewScreenInteractor
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(uiState.items) { index, repository ->
            Row(modifier = Modifier.clickable { overviewScreenInteractor.showDetails(repository.id) }) {
                Text("${index + 1}")
                Text(" ${repository.name}")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GitHubOverViewLoadingPreview() {
    GithubExplorerTheme {
        GitHubOverView(
            uiState = UiState.Overview.OverviewLoading,
            overviewScreenInteractor = DummyInteraction
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubOverViewErrorPreview() {
    GithubExplorerTheme {
        GitHubOverView(
            uiState = UiState.Overview.OverviewLoadingError,
            overviewScreenInteractor = DummyInteraction
        )
    }
}

private val DummyInteraction = object : OverviewScreenInteractor {
    override fun onRetryLoading() = Unit
    override fun showDetails(id: RepositoryId) = Unit
}