package nl.bluevoid.githubexplorer.domain.usecase

import nl.bluevoid.githubexplorer.data.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.util.ResultState
import nl.bluevoid.githubexplorer.domain.util.toResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetGithubRepositoriesUsecase(val gitHubDataRemoteRepository: GitHubDataRemoteRepository) {

    private val data = MutableStateFlow<ResultState<List<Repository>>>(ResultState.Loading)

    private val scope = CoroutineScope(Dispatchers.IO)

    // Returns a flow that emits user data whenever there’s an update
    operator fun invoke(): Flow<ResultState<List<Repository>>> {
        fetchData()
        return data
    }

    fun fetchData() {
        scope.launch {
            data.value = ResultState.Loading
            val result = gitHubDataRemoteRepository.getGitHubRepositories()
            data.value = result.toResultState()
        }
    }
}