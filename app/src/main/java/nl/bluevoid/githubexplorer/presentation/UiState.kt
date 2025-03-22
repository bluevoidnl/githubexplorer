package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.Repository

sealed interface UiState {
    data object OverviewLoading : UiState
    data class Overview(val items: List<Any>) : UiState
    data class Detail(val selectedRepository: Repository) : UiState
}