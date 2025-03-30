package nl.bluevoid.githubexplorer.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.bluevoid.githubexplorer.R
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.presentation.RepositoryDetailViewInteraction
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubDetailView(
    modifier: Modifier = Modifier,
    uiState: UiState.Detail,
    interaction: RepositoryDetailViewInteraction
) {
    val repository = uiState.selectedRepository

    BackHandler(onBack = { interaction.closeDetails() })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.repository, repository.name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { interaction.closeDetails() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DetailView(
                repository = repository,
                onOpenInBrowserClick = interaction::openInBrowser
            )
        }
    }
}

@Composable
private fun DetailView(
    modifier: Modifier = Modifier,
    repository: DomainRepository,
    onOpenInBrowserClick: (DomainRepository) -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        with(repository) {
            AvatarImage(imageUrl = ownerAvatarUrl.url, size = 150.dp)
            Text(stringResource(R.string.full_name, fullName))
            Text(stringResource(R.string.description, description ?: ""))
            Text(stringResource(R.string.is_public, isPublic))
            Text(stringResource(R.string.visibility, visibility.name.lowercase()))

            Button(onClick = { onOpenInBrowserClick(repository) }) {
                Text(stringResource(R.string.view_repository))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailViewPreview() {
    GithubExplorerTheme {
        DetailView(repository = PreviewData.REPOSITORY) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubDetailPreview() {
    val repository = PreviewData.REPOSITORY
    GithubExplorerTheme {
        GitHubDetailView(
            uiState = UiState.Detail(repository), interaction = object : RepositoryDetailViewInteraction {
                override fun closeDetails() = Unit
                override fun openInBrowser(repository: DomainRepository) = Unit
            }
        )
    }
}