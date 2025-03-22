package nl.bluevoid.githubexplorer.presentation

sealed interface UiState {
    data class Overview(val items:List<Any>): UiState
}