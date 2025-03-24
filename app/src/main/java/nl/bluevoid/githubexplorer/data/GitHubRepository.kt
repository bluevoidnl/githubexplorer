package nl.bluevoid.githubexplorer.data

import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.util.ResultState
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    fun getRepositoriesFlow(): Flow<ResultState<List<DomainRepository>>>
    suspend fun reload(networkAvailable:Boolean)
}