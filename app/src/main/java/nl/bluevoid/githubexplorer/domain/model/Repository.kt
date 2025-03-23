package nl.bluevoid.githubexplorer.domain.model

data class Repository(
    val id: RepositoryId,
    val name: String,
    val fullName: String,
    val description: String? = null,
    val ownerAvatarUrl: String,
    val visibility: Visibility,
    val repositoryLink: String
) {
    val isPublic = visibility != Visibility.Public
}

@JvmInline
value class RepositoryId(val id: Long)

enum class Visibility {
    Public, Private, Internal, Unknown
}