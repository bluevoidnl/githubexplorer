package nl.bluevoid.githubexplorer

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import nl.bluevoid.githubexplorer.data.GithubRepositoryImpl
import nl.bluevoid.githubexplorer.data.local.RepositoryDao
import nl.bluevoid.githubexplorer.data.remote.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.data.remote.Owner
import nl.bluevoid.githubexplorer.data.remote.Repository
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class GitHubRepositoryTest {

    private val remoteRepository = mockk<GitHubDataRemoteRepository>()
    private val database = mockk<RepositoryDao>()
    private val githubRepository = GithubRepositoryImpl(remoteRepository, database)

    @Before
    fun setup() {
        coEvery { remoteRepository.getGitHubRepositories() } returns Result.success(TEST_ITEMS)
        coEvery { database.getAllRepositories() } returns TEST_ITEMS.map { it.toEntity() }
        coEvery { database.upsertRepositories(any()) } returns Unit
    }

    @Test
    fun `when network is available fetch data and store`() = runTest {
        // When
        githubRepository.reload(true)

        // Then
        coVerify(exactly = 1) { remoteRepository.getGitHubRepositories() }
        coVerify(exactly = 1) { database.upsertRepositories(any()) }
        // Ensure no other calls were made to these mocks
        confirmVerified(remoteRepository, database)
    }

    @Test
    fun `when network is NOT available fetch data from store`()= runTest{
        // When
        githubRepository.reload(false)

        // Then
        coVerify(exactly = 1) { database.getAllRepositories() }
        // Ensure no other calls were made to these mocks
        confirmVerified(remoteRepository, database)
    }

    companion object {

        private val REPOSITORY_1 = Repository(
            id = 1,
            name = "1",
            full_name = "One for real",
            description = "One for real",
            visibility = "public",
            html_url = "https://github.com/one",
            owner = Owner(id = 1, avatar_url = "avatar url")
        )

        private val REPOSITORY_2 = Repository(
            id = 2,
            name = "2",
            full_name = "Two for real",
            description = "Two for real",
            visibility = "private",
            html_url = "https://github.com/two",
            owner = Owner(id = 2, avatar_url = "avatar url 2")
        )

        val TEST_ITEMS = listOf(REPOSITORY_1, REPOSITORY_2)

    }
}