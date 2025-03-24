package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.DomainRepository

sealed interface UiState {

    sealed interface Overview : UiState {
        data object OverviewLoading : Overview
        data object OverviewLoadingError : Overview
        data class OverviewItems(val items: List<DomainRepository>) : Overview
    }

    data class Detail(val selectedRepository: DomainRepository) : UiState
}