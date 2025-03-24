package nl.bluevoid.githubexplorer.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val html_url: String,
    val visibility: String,
    @Embedded val owner: OwnerEntity
) {
    fun toDomain(): DomainRepository {
        val visibilityEnum = try {
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
            visibility = visibilityEnum,
            ownerAvatarUrl = owner.avatar_url
        )
    }
}

data class OwnerEntity(
    val ownerId: Long,
    val avatar_url: String
)