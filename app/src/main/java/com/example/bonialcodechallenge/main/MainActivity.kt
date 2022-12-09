package com.example.bonialcodechallenge.main

import android.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bonialcodechallenge.R
import com.example.bonialcodechallenge.R.*
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

        setupUi()

        lifecycleScope.launchWhenCreated {
            viewModel.mainUiState.collectLatest { uiState ->
                when (uiState) {
                    is MainUiState.Success -> handleSuccessState(uiState)
                    MainUiState.Empty -> handleEmptyState()
                    MainUiState.Loading -> handleLoadingState(true)
                    is MainUiState.Error -> handleErrorState(uiState)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(com.example.bonialcodechallenge.R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.noFilter -> {
                viewModel.applyFilter(Filter.NO_FILTER)
            }
            id.closerThan5Filter -> {
                viewModel.applyFilter(Filter.CLOSER_THAN_5_FILTER)
            }
        }
        return true
    }

    private fun setupUi() {
        binding.brochureList.adapter = adapter
        binding.brochureList.layoutManager = GridLayoutManager(this, resources.getInteger(integer.grid_column_count))
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchBrochures()
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