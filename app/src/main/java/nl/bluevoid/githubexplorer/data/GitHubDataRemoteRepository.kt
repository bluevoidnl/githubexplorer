package nl.bluevoid.githubexplorer.data

import nl.bluevoid.githubexplorer.domain.model.Repository

interface GitHubDataRemoteRepository {

    suspend fun getGitHubRepositories(): Result<List<Repository>>
}