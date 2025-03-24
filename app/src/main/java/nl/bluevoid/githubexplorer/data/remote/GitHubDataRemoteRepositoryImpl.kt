package nl.bluevoid.githubexplorer.data.remote

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
import kotlinx.serialization.json.Json

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

    override suspend fun getGitHubRepositories(): Result<List<Repository>> {
        // todo see techdebt.MD, max 100 pages allowed per request as per api documentation
        return getRepositories("abnamrocoesd", 1, 100)
    }

    private suspend fun getRepositories(
        username: String,
        page: Int,
        perPage: Int
    ): Result<List<Repository>> {
        return try {
            val response = client.get("https://api.github.com/users/$username/repos") {
                parameter("page", page)
                parameter("per_page", perPage)
                header(HttpHeaders.Accept, "application/vnd.github.v3+json")
            }
            val repositories = response.body() as List<Repository>
            Result.success(repositories)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}