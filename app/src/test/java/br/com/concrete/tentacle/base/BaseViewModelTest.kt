package br.com.concrete.tentacle.base

import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.junit.MockitoJUnitRunner

@Ignore
@RunWith(MockitoJUnitRunner::class)
open class BaseViewModelTest : Instrumentation(), KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

}