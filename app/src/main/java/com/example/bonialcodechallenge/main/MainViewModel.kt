package com.example.bonialcodechallenge.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bonialcodechallenge.data.Repository
import com.example.bonialcodechallenge.data.models.Brochure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    init {
        fetchBrochures()
    }

    fun fetchBrochures() {
        viewModelScope.launch {
            _mainUiState.emit(MainUiState.Loading)
            val response = async(ioDispatcher) { requestBrochures() }
            response.await()
                .onSuccess {
                    if (it.isNullOrEmpty()) {
                        _mainUiState.emit(MainUiState.Empty)
                    } else {
                        _mainUiState.emit(MainUiState.Success(it))
                    }
                }
                .onFailure {
                    _mainUiState.emit(MainUiState.Error(it))
                }
        }
    }

    private suspend fun requestBrochures(): Result<List<Brochure>?> {
        return repository.getBrochures()
    }

    fun applyFilter(filter: Filter) {
        when(filter) {
            Filter.NO_FILTER -> {

            }
            Filter.CLOSER_THAN_5_FILTER -> {

            }
        }
    }
}