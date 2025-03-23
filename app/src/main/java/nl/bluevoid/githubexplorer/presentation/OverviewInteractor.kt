package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.RepositoryId

interface OverviewScreenInteractor {
    fun onRetryLoading()
    fun showDetails(id: RepositoryId)
}