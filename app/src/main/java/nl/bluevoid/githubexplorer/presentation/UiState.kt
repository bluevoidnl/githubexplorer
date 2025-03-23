package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.Repository

sealed interface UiState {

    sealed interface Overview : UiState {
        data object OverviewLoading : Overview
        data object OverviewLoadingError : Overview
        data class OverviewItems(val items: List<Repository>) : Overview
    }

    data class Detail(val selectedRepository: Repository) : UiState
}