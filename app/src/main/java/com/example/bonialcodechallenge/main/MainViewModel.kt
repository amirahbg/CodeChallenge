package com.example.bonialcodechallenge.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bonialcodechallenge.data.BrochureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val brochureRepository: BrochureRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    private val _brochuresFlow = MutableStateFlow(BrochureFilter.NO_FILTER)

    init {
        fetchBrochures()
        loadBrochures()
    }

    fun fetchBrochures() {
        viewModelScope.launch {
            brochureRepository
                .fetchBrochures()
                .onStart {
                    _mainUiState.emit(MainUiState.Loading)
                }
                .collectLatest {
                    it
                        .onSuccess {

                        }
                        .onFailure {
                            _mainUiState.emit(MainUiState.Error(it))
                        }

                }
        }
    }

    private fun loadBrochures() {
        viewModelScope.launch {
            _brochuresFlow
                .flatMapLatest {
                    brochureRepository.getBrochures(it)
                }
                .map {
                    if (it.isEmpty()) {
                        MainUiState.Empty
                    } else {
                        MainUiState.Success(it)
                    }
                }
                .collectLatest {
                    _mainUiState.emit(it)
                }
        }
    }

    fun applyFilter(brochureFilter: BrochureFilter) {
        viewModelScope.launch {
            _brochuresFlow.emit(brochureFilter)
        }
    }

}