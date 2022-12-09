package com.example.bonialcodechallenge.data.local

import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BrochureLocalDataSourceImpl @Inject constructor() : BrochureLocalDataSource {

    private val _emitterBrochureFlow = MutableStateFlow<List<Brochure>>(emptyList())
    private val _receiverBrochureFlow = _emitterBrochureFlow.asStateFlow()

    override suspend fun saveBrochures(brochures: List<Brochure>) {
        _emitterBrochureFlow.emit(brochures)
    }

    override suspend fun getBrochures(
        filter: BrochureFilter
    ): Flow<List<Brochure>> {
        return _receiverBrochureFlow
            .flatMapLatest { brochures ->
                val filteredBrochures = applyFilter(
                    filter,
                    brochures
                )
                flowOf(filteredBrochures)
            }
    }

    private fun applyFilter(
        filter: BrochureFilter,
        items: List<Brochure>
    ): List<Brochure> {
        return items.filter {
            when (filter) {
                BrochureFilter.NO_FILTER -> true
                BrochureFilter.LESS_THAN_5_KM -> {
                    it.distance < 5
                }
            }
        }
    }

}