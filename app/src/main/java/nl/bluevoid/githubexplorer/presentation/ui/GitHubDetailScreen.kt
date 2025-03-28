package nl.bluevoid.githubexplorer.presentation.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Url
import nl.bluevoid.githubexplorer.domain.model.Visibility
import nl.bluevoid.githubexplorer.presentation.RepositoryDetailViewInteractor
import nl.bluevoid.githubexplorer.presentation.UiState
import nl.bluevoid.githubexplorer.presentation.ui.theme.GithubExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubDetailView(
    uiState: UiState.Detail,
    interactor: RepositoryDetailViewInteractor
) {
    val repository = uiState.selectedRepository

    BackHandler(onBack = { interactor.closeDetails() })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Repository: ${repository.name}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { interactor.closeDetails() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            with(repository) {
                AvatarImage(imageUrl = ownerAvatarUrl.url, size = 150.dp)
                Text("Full name: $fullName")
                Text("Description: ${description ?: ""}")
                Text("Is public: $isPublic")
                Text("Visibility: ${visibility.name.lowercase()}")

                Button(onClick = { interactor.openInBrowser(repository) }) {
                    Text("View repository")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubDetailPreview() {
    val repository = DomainRepository(
        id = RepositoryId(1),
        name = "encrypted-push-notification",
        fullName = "adnamrocoesd/encrypted-push-notification",
        description = "Though shall not read my notifications!",
        ownerAvatarUrl = Url("https://image.com/anyimage.jpg"),
        visibility = Visibility.PUBLIC,
        repositoryLink = Url("https://github.com/abnamrocoesd/encrypted-push-notification")
    )
    GithubExplorerTheme {
        GitHubDetailView(
            uiState = UiState.Detail(repository), DummyInteraction
        )
    }
}

private val DummyInteraction = object : RepositoryDetailViewInteractor {
    override fun closeDetails() = Unit
    override fun openInBrowser(repository: DomainRepository) = Unit
}