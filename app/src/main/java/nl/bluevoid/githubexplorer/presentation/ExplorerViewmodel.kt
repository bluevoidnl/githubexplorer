package nl.bluevoid.githubexplorer.presentation

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nl.bluevoid.githubexplorer.data.GithubRepository
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.util.NetworkMonitor
import nl.bluevoid.githubexplorer.domain.util.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExplorerViewmodel(
    private val githubRepository: GithubRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel(),
    OverviewScreenInteractor,
    RepositoryDetailViewInteractor {

    private val selectedRepositoryFlow = MutableStateFlow<RepositoryId?>(null)

    private val networkFlow = networkMonitor.getNetworkAvailabilityFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val uiState = combine(githubRepository.getRepositoriesFlow(),
        selectedRepositoryFlow) { repositoriesLoadResult, selected ->
        when (repositoriesLoadResult) {
            is ResultState.Loading -> UiState.Overview.OverviewLoading
            is ResultState.Failure -> UiState.Overview.OverviewLoadingError
            is ResultState.Success -> {
                if (selected == null) UiState.Overview.OverviewItems(repositoriesLoadResult.data)
                else getSelectedState(repositoriesLoadResult.data, selected)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Overview.OverviewLoading)

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            networkFlow.distinctUntilChanged().collect {
                githubRepository.reload(it)
            }
        }
    }

    private fun getSelectedState(
        repositories: List<DomainRepository>,
        selected: RepositoryId?
    ): UiState {
        // list of repositories is small, no fast search needed for now
        val repository = repositories.firstOrNull { it.id == selected }
        return if (repository == null) {
            // data was updated and repository was not present any more (deleted?)
            // todo: see techdebt.MD. discuss with PO: notify user that repository is not available anymore?
            UiState.Overview.OverviewItems(repositories)
        } else {
            UiState.Detail(repository)
        }
    }

    override fun onRetryLoading() {
        viewModelScope.launch {
            val isNetworkAvailable = networkFlow.last()
            githubRepository.reload(isNetworkAvailable)
        }
    }

    override fun showDetails(id: RepositoryId) {
        selectedRepositoryFlow.value = id
    }

    override fun closeDetails() {
        selectedRepositoryFlow.value = null
    }

    override fun openInBrowser(repository: DomainRepository) {
        try {
            _navigationEvents.tryEmit(NavigationEvent.OpenInAppBrowser(repository.repositoryLink.toUri()))
        } catch (e: Exception) {
            // todo handle incorrect urls
            e.printStackTrace()
        }
    }
}

sealed interface NavigationEvent {
    data class OpenInAppBrowser(val url: Uri) : NavigationEvent
}