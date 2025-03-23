package nl.bluevoid.githubexplorer.data.model

import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility
import kotlinx.serialization.Serializable
import nl.bluevoid.githubexplorer.domain.model.Repository as DomainRepository

@Serializable
data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String? = null,
    val html_url: String,
    val visibility: String,
    val owner: Owner
) {
    fun toDomainRepository(): DomainRepository {
        val visEnum = try {
            Visibility.valueOf(visibility.uppercase())
        } catch (e: Exception) {
            println("RRR unknown: $visibility")
            Visibility.UNKNOWN
        }

        return DomainRepository(
            RepositoryId(id),
            name = name,
            fullName = full_name,
            description = description,
            repositoryLink = html_url,
            visibility = visEnum,
            ownerAvatarUrl = owner.avatar_url
        )
    }
}

@Serializable
data class Owner(
    val avatar_url: String
)