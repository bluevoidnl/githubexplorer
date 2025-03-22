package nl.bluevoid.githubexplorer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            selected != null -> {
                val repository = repositories.firstOrNull { it.id == selected }
                if (repository == null) {
                    // data may be updated and repository was deleted
                    UiState.Overview(repositories)
                } else {
                    UiState.Detail(repository)
                }
            }

            else -> UiState.Overview(repositories)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Overview(emptyList()))

    fun showDetails(id: RepositoryId) {
        selectedRepositoryFlow.value = id
    }
}