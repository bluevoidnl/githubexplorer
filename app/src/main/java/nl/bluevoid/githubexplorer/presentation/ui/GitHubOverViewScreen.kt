package nl.bluevoid.githubexplorer.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.bluevoid.githubexplorer.R
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.presentation.OverviewScreenInteraction
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubOverView(
    modifier: Modifier = Modifier,
    uiState: UiState.Overview,
    overviewScreenInteraction: OverviewScreenInteraction
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.github_explorer_title)) },
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
                is UiState.Overview.OverviewLoading -> LoadingView()
                is UiState.Overview.OverviewItems -> GitHubListView(modifier, uiState, overviewScreenInteraction)
                is UiState.Overview.OverviewLoadingError -> LoadingErrorView(
                    onRetryClick = overviewScreenInteraction::onRetryLoading)

            }
        }
    }
}

@Composable
private fun LoadingErrorView(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.something_went_wrong_please_try_again),
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message)
            Button(onClick = { onRetryClick() }) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun GitHubListView(
    modifier: Modifier = Modifier,
    uiState: UiState.Overview.OverviewItems,
    overviewScreenInteraction: OverviewScreenInteraction
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(uiState.items,
            key = { _, repository -> repository.id.id }) { index, repository ->
            RepositoryItem(index = index,
                repository = repository,
                onClick = overviewScreenInteraction::showDetails)
        }
    }
}

@Composable
private fun RepositoryItem(
    modifier: Modifier = Modifier,
    index: Int,
    repository: DomainRepository,
    onClick: (RepositoryId) -> Unit
) {
    Row(modifier = modifier.clickable { onClick(repository.id) },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        with(repository) {
            AvatarImage(imageUrl = ownerAvatarUrl.url, size = 80.dp)
            Column {
                Text("${index + 1}. $name")
                Text(stringResource(R.string.visibility, visibility.name.lowercase()))
                Text(stringResource(R.string.is_public, isPublic))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubOverViewLoadingPreview() {
    GithubExplorerTheme {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubOverViewErrorPreview() {
    GithubExplorerTheme {
        LoadingErrorView() {}
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryItemPreview() {
    GithubExplorerTheme {
        RepositoryItem(
            index = 1,
            repository = PreviewData.REPOSITORY,
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubListViewPreview() {
    GithubExplorerTheme {
        GitHubListView(
            uiState = UiState.Overview.OverviewItems(PreviewData.REPOSITORY_ITEMS),
            overviewScreenInteraction = object : OverviewScreenInteraction {
                override fun onRetryLoading() = Unit
                override fun showDetails(id: RepositoryId) = Unit
            }
        )
    }
}

