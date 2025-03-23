package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility
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
    LazyColumn(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(uiState.items) { index, repository ->
            Row(modifier = Modifier.clickable { overviewScreenInteractor.showDetails(repository.id) },
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                with(repository) {
                    AvatarImage(imageUrl = ownerAvatarUrl, size = 80.dp)
                    Column {
                        Text("${index + 1}. $name")
                        Text("Visibility: ${visibility.name.lowercase()}")
                        Text("Is public: $isPublic")
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun GitHubListViewPreview() {
    GithubExplorerTheme {
        GitHubListView(
            uiState = UiState.Overview.OverviewItems(listOf(repository, repository2)),
            overviewScreenInteractor = DummyInteraction
        )
    }
}

private val repository = Repository(
    id = RepositoryId(1),
    name = "encrypted-push-notification",
    fullName = "adnamrocoesd/encrypted-push-notification",
    description = "Though shall not read my notifications!",
    ownerAvatarUrl = "https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg",
    visibility = Visibility.PUBLIC,
    repositoryLink = "https://github.com/abnamrocoesd/encrypted-push-notification"
)

private val repository2 = Repository(
    id = RepositoryId(2),
    name = "external-storage",
    fullName = "adnamrocoesd/external-storage",
    description = "",
    ownerAvatarUrl = "https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg",
    visibility = Visibility.PUBLIC,
    repositoryLink = "https://github.com/abnamrocoesd/external-storage"
)

private val DummyInteraction = object : OverviewScreenInteractor {
    override fun onRetryLoading() = Unit
    override fun showDetails(id: RepositoryId) = Unit
}