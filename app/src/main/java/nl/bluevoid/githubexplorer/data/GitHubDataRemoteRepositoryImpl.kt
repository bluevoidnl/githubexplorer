package nl.bluevoid.githubexplorer.data

import nl.bluevoid.githubexplorer.domain.model.Repository

class GitHubDataRemoteRepositoryImpl : GitHubDataRemoteRepository {
    override suspend fun getGitHubRepositories(): Result<List<Repository>> {
        return Result.failure(NotImplementedError())
    }
}