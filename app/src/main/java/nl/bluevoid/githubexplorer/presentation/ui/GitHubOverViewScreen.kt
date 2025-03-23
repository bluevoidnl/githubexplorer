package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme

@Composable
fun GitHubOverView(modifier: Modifier = Modifier, uiState: UiState.Overview) {
    when (uiState) {
        is UiState.Overview.OverviewLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }

        is UiState.Overview.OverviewItems -> GitHubListView(modifier, uiState)
        is UiState.Overview.OverviewLoadingError -> Text("Something went wrong please try again")
    }
}

@Composable
fun GitHubListView(modifier: Modifier = Modifier, uiState: UiState.Overview.OverviewItems) {
    LazyColumn {
        itemsIndexed(uiState.items) { index, repository ->
            Row() {
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
        GitHubOverView(uiState = UiState.Overview.OverviewLoading)
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubOverViewErrorPreview() {
    GithubExplorerTheme {
        GitHubOverView(uiState = UiState.Overview.OverviewLoadingError)
    }
}