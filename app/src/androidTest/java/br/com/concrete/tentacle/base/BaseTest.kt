package br.com.concrete.tentacle.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.concrete.tentacle.di.*
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.KoinProperties
import org.koin.standalone.StandAloneContext

@RunWith(AndroidJUnit4::class)
open class BaseTest {

    @Before
    fun before(){
        StandAloneContext.stopKoin()
        StandAloneContext.startKoin(listOf(
            networkModule,
            viewModelModule,
            repositoryModule,
            sharedPreferencesModule
        ), properties = KoinProperties(extraProperties = mapOf(PROPERTY_BASE_URL to "/")))
    }

    @After
    fun after(){
        StandAloneContext.stopKoin()
    }
}