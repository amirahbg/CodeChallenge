package com.example.bonialcodechallenge

import com.example.bonialcodechallenge.data.BrochureRepository
import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository(
    var fakeData: List<Brochure>, var testScenarios: TestScenarios = TestScenarios.Empty
) : BrochureRepository {

    override suspend fun fetchBrochures(): Flow<Result<Unit>> = flow {
        when (testScenarios) {
            TestScenarios.Success -> emit(Result.success(Unit))
            TestScenarios.Empty -> emit(Result.success(Unit))
            TestScenarios.Failed -> emit(Result.failure(FakeException()))
        }
    }

    override suspend fun getBrochures(filter: BrochureFilter): Flow<List<Brochure>> = flow {
        when (testScenarios) {
            TestScenarios.Empty -> emit(emptyList())
            TestScenarios.Success -> emit(fakeData)
        }
    }
}

sealed class TestScenarios {
    object Empty : TestScenarios()
    object Success : TestScenarios()
    object Failed : TestScenarios()
}

