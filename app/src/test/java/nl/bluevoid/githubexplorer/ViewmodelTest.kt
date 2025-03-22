package nl.bluevoid.githubexplorer

import io.mockk.coEvery
import io.mockk.mockk
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubDataUsecase
import nl.bluevoid.githubexplorer.presentation.ExplorerViewmodel
import nl.bluevoid.githubexplorer.presentation.UiState
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test as testFlow

class ViewmodelTest {

    // Mock Repository
    private val gitHubUsecase: GetGithubDataUsecase = mockk()

    @Before
    fun setup() {
        coEvery { gitHubUsecase.invoke() } returns MutableStateFlow(TEST_ITEMS)
    }

    @Test
    fun `when initialized then viewmodel should return overview`() = runTest {
        // Given
        val vm = getViewModel()

        // Then
        vm.uiState.testFlow {
            val value = awaitItem()
            assertTrue(value is UiState.Overview)
            // Skip the next event where the list of items is delivered to the overview
            skipItems(1)
        }
    }

    @Test
    fun `when initialized then viewmodel should retrieve overview data`() = runTest {
        // Given
        val vm = getViewModel()

        // When
        // wait till non empty list of items is received
        val flowWithOverviewData = vm.uiState.filter { it is UiState.Overview && it.items.isNotEmpty() }

        // Then
        flowWithOverviewData.testFlow {
            val value = awaitItem()
            val items = (value as UiState.Overview).items
            assertTrue(items == TEST_ITEMS)
        }
    }

    @Test
    fun `when a user selects a repository then viewmodel returns the details uiState with the correct details `() =
        runTest {
            // Given
            val vm = getViewModel()

            // When
            vm.showDetails(REPOSITORY_2.id)

            // Then
            vm.uiState.filter { it is UiState.Detail }.testFlow {
                val value = awaitItem()
                val selectedRepository = (value as UiState.Detail).selectedRepository
                assertTrue(selectedRepository == REPOSITORY_2)
            }
        }

    @Test
    fun `given a user views details when details are closed then viewmodel returns the overview uiState`() =
        runTest {
            // Given
            val vm = getViewModel()
            vm.showDetails(REPOSITORY_2.id)

            // When
            vm.closeDetails()

            // Then
            val flowWithOverviewData = vm.uiState.filter { it is UiState.Overview }
            flowWithOverviewData.testFlow {
                val value = awaitItem()
                val items = (value as UiState.Overview).items
                assertTrue(items == TEST_ITEMS)
            }
        }

    private fun getViewModel(): ExplorerViewmodel {
        return ExplorerViewmodel(gitHubUsecase)
    }

    companion object {

        private val REPOSITORY_1 = Repository(
            id = RepositoryId(1),
            name = "1",
            fullName = "One for real",
            description = "One for real",
            ownerAvatarUrl = "ownerAvatarUrlOne",
            visibility = Visibility.Public,
            repositoryLink = "https://github.com/one"
        )

        private val REPOSITORY_2 = Repository(
            id = RepositoryId(2),
            name = "2",
            fullName = "Two for real",
            description = "Two for real",
            ownerAvatarUrl = "ownerAvatarUrlTwo",
            visibility = Visibility.Private,
            repositoryLink = "https://github.com/two"
        )

        val TEST_ITEMS = listOf(REPOSITORY_1, REPOSITORY_2)

    }
}