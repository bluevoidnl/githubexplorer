package nl.bluevoid.githubexplorer.domain.usecase

import nl.bluevoid.githubexplorer.data.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.domain.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetGithubDataUsecase(val gitHubDataRemoteRepository: GitHubDataRemoteRepository) {

    private val data = MutableStateFlow<Result<List<Repository>>>(Result.success(emptyList()))

    // Returns a flow that emits user data whenever there’s an update
    operator fun invoke(): Flow<Result<List<Repository>>> {
        fetchData()
        return data
    }

    private fun fetchData() {
        //  gitHubDataRemoteRepository.getGitHubRepositories()
    }
}