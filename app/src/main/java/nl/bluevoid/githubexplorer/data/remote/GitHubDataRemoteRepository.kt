package nl.bluevoid.githubexplorer.data.remote

interface GitHubDataRemoteRepository {
    suspend fun getGitHubRepositories(): Result<List<Repository>>
}