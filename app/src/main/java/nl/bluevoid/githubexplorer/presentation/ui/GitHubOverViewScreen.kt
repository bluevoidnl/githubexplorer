package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme


@Composable
fun GitHubOverView(uiState: UiState.Overview) {
    when (uiState) {
        is UiState.Overview.OverviewLoading -> Text("Loading...")
        is UiState.Overview.OverviewItems -> GitHubListView(uiState)
        is UiState.Overview.OverviewLoadingError -> Text("Something went wrong please try again")
    }
}

@Composable
fun GitHubListView(uiState: UiState.Overview.OverviewItems) {
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
fun GreetingPreview() {
    GithubExplorerTheme {

    }
}