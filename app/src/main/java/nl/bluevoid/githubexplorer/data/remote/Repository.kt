package nl.bluevoid.githubexplorer.data.remote

import nl.bluevoid.githubexplorer.data.local.OwnerEntity
import nl.bluevoid.githubexplorer.data.local.RepositoryEntity
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility
import kotlinx.serialization.Serializable

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
    fun toDomain(): DomainRepository {
        val visEnum = try {
            Visibility.valueOf(visibility.uppercase())
        } catch (e: Exception) {
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

    fun toEntity() = RepositoryEntity(
        id = id,
        name = name,
        full_name = full_name,
        description = description,
        html_url = html_url,
        visibility = visibility,
        owner = owner.toEntity()
    )
}

@Serializable
data class Owner(
    val id: Long,
    val avatar_url: String
) {
    fun toEntity() = OwnerEntity(id, avatar_url)
}