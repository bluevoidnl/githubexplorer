package nl.bluevoid.githubexplorer.data

import nl.bluevoid.githubexplorer.data.local.RepositoryDao
import nl.bluevoid.githubexplorer.data.remote.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.data.remote.Repository
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.util.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GithubRepository(
    private val remoteRepository: GitHubDataRemoteRepository,
    private val repositoryDao: RepositoryDao
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val repositoriesFlow = MutableStateFlow<ResultState<List<DomainRepository>>>(ResultState.Loading)

    fun getRepositoriesFlow() = repositoriesFlow.asStateFlow()

    suspend fun reload(networkAvailable: Boolean) {
        repositoriesFlow.value = ResultState.Loading
        repositoriesFlow.value = if (networkAvailable) {
            fetchAndStore()
        } else {
            val repositories = repositoryDao.getAllRepositories().map { it.toDomain() }
            ResultState.Success(repositories)
        }
    }

    private suspend fun fetchAndStore(): ResultState<List<DomainRepository>> {
        val repositoriesResult = remoteRepository.getGitHubRepositories()
        return if (repositoriesResult.isSuccess) {
            val repositories = repositoriesResult.getOrNull()
            if (repositories == null) {
                ResultState.Failure(null)
                // todo: log error, this should not happen
            } else {
                store(repositories)
                ResultState.Success(repositories.map { it.toDomain() })
            }
        } else {
            ResultState.Failure(repositoriesResult.exceptionOrNull())
        }
    }

    private fun store(repositories: List<Repository>) {
        scope.launch {
            repositoryDao.upsertRepositories(repositories.map { it.toEntity() })
        }
    }
}