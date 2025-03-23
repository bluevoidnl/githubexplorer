package nl.bluevoid.githubexplorer.presentation

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubRepositoriesUsecase
import nl.bluevoid.githubexplorer.domain.util.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ExplorerViewmodel(private val getGithubDataUsecase: GetGithubRepositoriesUsecase) : ViewModel(),
    OverviewScreenInteractor,
    RepositoryDetailViewInteractor {

    private val selectedRepositoryFlow = MutableStateFlow<RepositoryId?>(null)

    val uiState = combine(getGithubDataUsecase.invoke(), selectedRepositoryFlow) { repositoriesLoadResult, selected ->
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

    private fun getSelectedState(
        repositories: List<Repository>,
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
        getGithubDataUsecase.fetchData()
    }

    override fun showDetails(id: RepositoryId) {
        selectedRepositoryFlow.value = id
    }

    override fun closeDetails() {
        selectedRepositoryFlow.value = null
    }

    override fun openInBrowser(repository: Repository) {
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