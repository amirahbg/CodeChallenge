package com.example.bonialcodechallenge

import com.example.bonialcodechallenge.data.Repository
import com.example.bonialcodechallenge.data.models.Brochure

class FakeRepository(
    var fakeData: List<Brochure>, var testScenarios: TestScenarios = TestScenarios.Empty
) : Repository {

    override suspend fun getBrochures(): Result<List<Brochure>?> {
        return when (testScenarios) {
            TestScenarios.Success -> Result.success(fakeData)
            TestScenarios.Empty -> Result.success(listOf())
            TestScenarios.Failed -> Result.failure(FakeException())
        }
    }
}

sealed class TestScenarios {
    object Empty : TestScenarios()
    object Success : TestScenarios()
    object Failed : TestScenarios()
}

