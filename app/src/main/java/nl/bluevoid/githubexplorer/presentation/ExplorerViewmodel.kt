package nl.bluevoid.githubexplorer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubDataUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExplorerViewmodel(getGithubDataUsecase: GetGithubDataUsecase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Overview(emptyList()))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getGithubDataUsecase.invoke().collect { repositories ->
                _uiState.value = when (_uiState.value) {
                    is UiState.Overview -> UiState.Overview(repositories)
                }
            }
        }
    }
}