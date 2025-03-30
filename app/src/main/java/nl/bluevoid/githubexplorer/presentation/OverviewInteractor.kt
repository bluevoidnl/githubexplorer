package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.RepositoryId

interface OverviewScreenInteraction {
    fun onRetryLoading()
    fun showDetails(id: RepositoryId)
}