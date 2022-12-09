package com.example.bonialcodechallenge.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bonialcodechallenge.R
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
        binding.brochureList.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.grid_column_count))
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchBrochures()
        }

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
        binding.brochureList.visibility = VISIBLE
        binding.errorLayout.root.visibility = GONE
        adapter.submitList(uiState.brochures)
        handleLoadingState(false)
    }

    private fun handleEmptyState() {
        binding.brochureList.visibility = GONE
        binding.errorLayout.root.visibility = VISIBLE
        binding.errorLayout.errorMessage.setText(R.string.empty_list_error)
        handleLoadingState(false)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun handleErrorState(uiState: MainUiState.Error) {
        binding.brochureList.visibility = GONE
        binding.errorLayout.root.visibility = VISIBLE
        binding.errorLayout.errorMessage.text = uiState.exception.message
        handleLoadingState(false)
    }
}