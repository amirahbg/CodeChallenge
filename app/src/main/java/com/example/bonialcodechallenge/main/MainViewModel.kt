package com.example.bonialcodechallenge.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bonialcodechallenge.data.BrochureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val brochureRepository: BrochureRepository,
) : ViewModel() {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    private val _emitterBrochuresFlow = MutableStateFlow(BrochureFilter.NO_FILTER)
    private val _brochuresFlow = _emitterBrochuresFlow.asStateFlow()

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
                    it.onFailure { throwable ->
                            _mainUiState.emit(MainUiState.Error(throwable))
                        }
                }
        }
    }

    @VisibleForTesting
    fun loadBrochures() {
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
            _emitterBrochuresFlow.emit(brochureFilter)
        }
    }

}