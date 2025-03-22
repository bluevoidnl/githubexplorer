package nl.bluevoid.githubexplorer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubDataUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ExplorerViewmodel(getGithubDataUsecase: GetGithubDataUsecase) : ViewModel() {

    private val selectedRepositoryFlow = MutableStateFlow<RepositoryId?>(null)

    val uiState = combine(getGithubDataUsecase.invoke(), selectedRepositoryFlow) { repositories, selected ->
        when {
            selected == null -> UiState.Overview(repositories)
            else -> getSelectedState(repositories, selected)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.OverviewLoading)

    private fun getSelectedState(
        repositories: List<Repository>,
        selected: RepositoryId?
    ): UiState {
        // list of repositories is small, no fast search needed for now
        val repository = repositories.firstOrNull { it.id == selected }
        return if (repository == null) {
            // data was updated and repository was not present any more (deleted?)
            // todo: (out of scope for now) discuss with PO & notify user that repository is not available anymore
            UiState.Overview(repositories)
        } else {
            UiState.Detail(repository)
        }
    }

    fun showDetails(id: RepositoryId) {
        selectedRepositoryFlow.value = id
    }

    fun closeDetails() {
        selectedRepositoryFlow.value = null
    }
}