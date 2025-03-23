package nl.bluevoid.githubexplorer.domain.usecase

import nl.bluevoid.githubexplorer.data.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.domain.model.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetGithubDataUsecase(val gitHubDataRemoteRepository: GitHubDataRemoteRepository) {

    private val data = MutableStateFlow<Result<List<Repository>>>(Result.success(emptyList()))

    private val scope = CoroutineScope(Dispatchers.IO)

    // Returns a flow that emits user data whenever there’s an update
    operator fun invoke(): Flow<Result<List<Repository>>> {
        fetchData()
        return data
    }

    private fun fetchData() {
        scope.launch {
            data.value = gitHubDataRemoteRepository.getGitHubRepositories()
        }
    }
}