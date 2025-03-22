package nl.bluevoid.githubexplorer.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExplorerViewmodel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Overview(emptyList()))
    val uiState = _uiState.asStateFlow()
}