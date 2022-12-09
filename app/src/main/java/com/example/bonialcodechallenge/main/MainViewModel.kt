package com.example.bonialcodechallenge.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bonialcodechallenge.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _brochures = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val brochures: StateFlow<MainUiState> = _brochures

    fun fetchBrochures() {
        viewModelScope.launch {
            _brochures.emit(MainUiState.Loading)
            repository.getBrochures()
                .onSuccess {
                    if (it.isNullOrEmpty()) {
                        _brochures.emit(MainUiState.Empty)
                    } else {
                        _brochures.emit(MainUiState.Success(it))
                    }
                }
                .onFailure {
                    _brochures.emit(MainUiState.Error(it))
                }
        }
    }
}