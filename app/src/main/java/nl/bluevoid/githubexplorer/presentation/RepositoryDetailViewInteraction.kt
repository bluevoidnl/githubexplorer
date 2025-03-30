package nl.bluevoid.githubexplorer.presentation

import nl.bluevoid.githubexplorer.domain.model.DomainRepository

interface RepositoryDetailViewInteraction {
    fun closeDetails()
    fun openInBrowser(repository: DomainRepository)
}