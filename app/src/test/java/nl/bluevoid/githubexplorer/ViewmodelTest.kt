package nl.bluevoid.githubexplorer

import nl.bluevoid.githubexplorer.presentation.ExplorerViewmodel
import nl.bluevoid.githubexplorer.presentation.UiState
import org.junit.Test

import org.junit.Assert.*

class ViewmodelTest {
    @Test
    fun `when initialized then viewmodel should return overview`() {
        val vm = ExplorerViewmodel()
        assertTrue(vm.uiState.value is UiState.Overview)
    }
}