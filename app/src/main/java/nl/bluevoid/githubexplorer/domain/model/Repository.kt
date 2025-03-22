package nl.bluevoid.githubexplorer.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String,
    val ownerAvatarUrl: String,
    val visibility: Visibility,
    val repositoryLink: String
) {
    /**
     * If private is true, the repository is restricted to specific users or teams with explicit access (e.g., the owner
     * and invited collaborators).
     * If private is false, the repository is accessible to a broader audience, but this doesn’t fully specify how
     * accessible it is—it could be public or, in some cases, internal (for enterprise accounts).
     *
     * This field is there for github historical reasons
     */
    val isPublic = visibility != Visibility.Public
}

enum class Visibility {
    Public, Private, Internal
}