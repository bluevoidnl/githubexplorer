package nl.bluevoid.githubexplorer.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import nl.bluevoid.githubexplorer.data.model.Repository
import kotlinx.serialization.json.Json
import nl.bluevoid.githubexplorer.domain.model.Repository as DomainRepository

class GitHubDataRemoteRepositoryImpl : GitHubDataRemoteRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL // Logs network requests
        }
    }

    override suspend fun getGitHubRepositories(): Result<List<DomainRepository>> {
        // todo see techdebt.MD, max 100 pages allowed per request as per api documentation
        return getRepositories("abnamrocoesd", 1, 100)
    }

    private suspend fun getRepositories(
        username: String,
        page: Int,
        perPage: Int
    ): Result<List<DomainRepository>> {
        return try {
            val response = client.get("https://api.github.com/users/$username/repos") {
                parameter("page", page)
                parameter("per_page", perPage)
                header(HttpHeaders.Accept, "application/vnd.github.v3+json")
            }
            val repositories = response.body() as List<Repository>
            val domainRepos = repositories.map { it.toDomainRepository() }
            Result.success(domainRepos)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}