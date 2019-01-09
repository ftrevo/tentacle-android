package br.com.concrete.tentacle.base

import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.concrete.tentacle.repositories.SharedPrefRepository
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@Ignore
@RunWith(MockitoJUnitRunner::class)
open class BaseViewModelTest : Instrumentation(), KoinTest {

    @Mock
    lateinit var sharePrefRepository: SharedPrefRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
}