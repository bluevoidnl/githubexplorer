package nl.bluevoid.githubexplorer.domain.model

data class DomainRepository(
    val id: RepositoryId,
    val name: String,
    val fullName: String,
    val description: String? = null,
    val ownerAvatarUrl: Url,
    val visibility: Visibility,
    val repositoryLink: Url
) {
    val isPublic = visibility == Visibility.PUBLIC
}

@JvmInline
value class RepositoryId(val id: Long)

@JvmInline
value class Url(val url: String)

enum class Visibility {
    PUBLIC, PRIVATE, INTERNAL, UNKNOWN
}