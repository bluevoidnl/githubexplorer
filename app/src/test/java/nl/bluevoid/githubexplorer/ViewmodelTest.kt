package nl.bluevoid.githubexplorer

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.RepositoryId
import nl.bluevoid.githubexplorer.domain.model.Visibility
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubRepositoriesUsecase
import nl.bluevoid.githubexplorer.domain.util.ResultState
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
    private val gitHubUseCase = mockk<GetGithubRepositoriesUsecase>()

    @Before
    fun setup() {
        coEvery { gitHubUseCase.invoke() } returns MutableStateFlow(ResultState.Success(TEST_ITEMS))
    }

    @Test
    fun `when initialized then viewmodel should return overview loading`() {
        // Given
        val vm = getViewModel()

        // Then
        assertTrue(vm.uiState.value is UiState.Overview.OverviewLoading)
    }

    @Test
    fun `when initialized then viewmodel should retrieve overview data`() = runTest {
        // Given
        val vm = getViewModel()

        // When
        // wait till non empty list of items is received
        val flowWithOverviewData = vm.uiState.filter { it is UiState.Overview.OverviewItems }

        // Then
        flowWithOverviewData.testFlow {
            val value = awaitItem()
            val items = (value as UiState.Overview.OverviewItems).items
            assertTrue(items == TEST_ITEMS)
        }
    }

    @Test
    fun `when data retrieval fails then viewmodel should return loading error state`() = runTest {
        // Given
        coEvery { gitHubUseCase.invoke() } returns MutableStateFlow(ResultState.Failure(IOException("In outer space")))

        val vm = getViewModel()

        // When

        // wait till error is received
        val flowWithOverviewError = vm.uiState.filter { it is UiState.Overview.OverviewLoadingError }

        // Then
        flowWithOverviewError.testFlow {
            val value = awaitItem()
            assertTrue(value is UiState.Overview.OverviewLoadingError)
        }
    }

    @Test
    fun `Given data retrieval failed when user reloads then usecase should reload data`() {
        // Given
        coEvery { gitHubUseCase.invoke() } returns MutableStateFlow(ResultState.Failure(IOException("In outer space")))
        every { gitHubUseCase.fetchData() } returns Unit
        val vm = getViewModel()
        // wait till error is received
        // val flowWithOverviewError = vm.uiState.filter { it is UiState.Overview.OverviewLoadingError }

        // When
        vm.onRetryLoading()

        // Then
        verify(exactly = 1) { gitHubUseCase.fetchData() }
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
            val flowWithOverviewData = vm.uiState.filter { it is UiState.Overview.OverviewItems }
            flowWithOverviewData.testFlow {
                val value = awaitItem()
                val items = (value as UiState.Overview.OverviewItems).items
                assertTrue(items == TEST_ITEMS)
            }
        }

    private fun getViewModel(): ExplorerViewmodel {
        return ExplorerViewmodel(gitHubUseCase)
    }

    companion object {

        private val REPOSITORY_1 = Repository(
            id = RepositoryId(1),
            name = "1",
            fullName = "One for real",
            description = "One for real",
            ownerAvatarUrl = "ownerAvatarUrlOne",
            visibility = Visibility.PUBLIC,
            repositoryLink = "https://github.com/one"
        )

        private val REPOSITORY_2 = Repository(
            id = RepositoryId(2),
            name = "2",
            fullName = "Two for real",
            description = "Two for real",
            ownerAvatarUrl = "ownerAvatarUrlTwo",
            visibility = Visibility.PRIVATE,
            repositoryLink = "https://github.com/two"
        )

        val TEST_ITEMS = listOf(REPOSITORY_1, REPOSITORY_2)

    }
}