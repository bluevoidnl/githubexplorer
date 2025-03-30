package nl.bluevoid.githubexplorer.presentation.ui

import nl.bluevoid.githubexplorer.domain.model.DomainRepository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Url
import nl.bluevoid.githubexplorer.domain.model.Visibility

object PreviewData {

    val REPOSITORY = DomainRepository(
        id = RepositoryId(1),
        name = "encrypted-push-notification",
        fullName = "adnamrocoesd/encrypted-push-notification",
        description = "Though shall not read my notifications!",
        ownerAvatarUrl = Url("https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg"),
        visibility = Visibility.PUBLIC,
        repositoryLink = Url("https://github.com/abnamrocoesd/encrypted-push-notification")
    )

    val REPOSITORY_2 = DomainRepository(
        id = RepositoryId(2),
        name = "external-storage",
        fullName = "adnamrocoesd/external-storage",
        description = "",
        ownerAvatarUrl = Url("https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg"),
        visibility = Visibility.PUBLIC,
        repositoryLink = Url("https://github.com/abnamrocoesd/external-storage")
    )

    val REPOSITORY_ITEMS = listOf(REPOSITORY, REPOSITORY_2)
}