package com.example.bonialcodechallenge

import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.main.MainUiState
import com.example.bonialcodechallenge.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setUp() {
        repository = FakeRepository(publishFakeBrochure())
        viewModel = MainViewModel(repository, mainCoroutineRule.testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun mainViewModel_errorScenario() = runTest {
        repository.testScenarios = TestScenarios.Failed
        var uiState = viewModel.mainUiState.value
        assert(uiState == MainUiState.Empty)

        viewModel.fetchBrochures()
        viewModel.loadBrochures()
        advanceUntilIdle()
        uiState = viewModel.mainUiState.value
        assert(uiState is MainUiState.Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun mainViewModel_emptyScenario() = runTest {
        repository.testScenarios = TestScenarios.Empty
        var uiState = viewModel.mainUiState.value
        assert(uiState == MainUiState.Empty)

        viewModel.fetchBrochures()
        viewModel.loadBrochures()
        advanceUntilIdle()
        uiState = viewModel.mainUiState.value
        assert(uiState is MainUiState.Empty)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun mainViewModel_SuccessScenario() = runTest {
        repository.testScenarios = TestScenarios.Success
        var uiState = viewModel.mainUiState.value
        assert(uiState == MainUiState.Empty)

        viewModel.fetchBrochures()
        viewModel.loadBrochures()
        advanceUntilIdle()
        uiState = viewModel.mainUiState.value
        assert(uiState is MainUiState.Success && uiState.brochures == publishFakeBrochure())
    }

    private fun publishFakeBrochure() = (0..10)
        .map { Brochure(it.toLong(), "$it", "$it", it % 2 == 0, it.toDouble()) }
}