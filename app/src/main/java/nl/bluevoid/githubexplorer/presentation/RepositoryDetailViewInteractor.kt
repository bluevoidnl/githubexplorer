package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.Repository

interface RepositoryDetailViewInteractor {
    fun closeDetails()
    fun openInBrowser(repository: Repository)
}