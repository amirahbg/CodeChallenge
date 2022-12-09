package com.example.bonialcodechallenge.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bonialcodechallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { BrochuresAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.brochureList.adapter = adapter
        binding.brochureList.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchBrochures()
        }

        viewModel.fetchBrochures()

        lifecycleScope.launchWhenCreated {
            viewModel.brochures.collectLatest { uiState ->
                when (uiState) {
                    is MainUiState.Success -> handleSuccessState(uiState)
                    MainUiState.Empty -> handleEmptyState()
                    MainUiState.Loading -> handleLoadingState(true)
                    is MainUiState.Error -> handleErrorState(uiState)
                }
            }
        }
    }

    private fun handleSuccessState(uiState: MainUiState.Success) {
        adapter.submitList(uiState.brochures)
        handleLoadingState(false)
    }

    private fun handleEmptyState() {

        handleLoadingState(false)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun handleErrorState(uiState: MainUiState.Error) {

        handleLoadingState(false)
    }

    companion object {
        const val SPAN_COUNT = 2
    }
}