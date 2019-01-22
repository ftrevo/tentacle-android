package br.com.concrete.tentacle.rule

import PROPERTY_BASE_URL
import br.com.concrete.tentacle.di.*
import android.app.Activity
import androidx.test.rule.ActivityTestRule
import networkModule
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin

class KoinActivityTestRule<T : Activity> constructor(
    activityClass: Class<T>,
    initialTouchMode: Boolean = true,
    launchActivity: Boolean = true
) : TestRule, KoinComponent {

    private val testName = TestName()
    val activityRule = ActivityTestRule(activityClass)
    val mockWebServer = MockWebServer()

    private val delegateRule = RuleChain
        .outerRule(testName)
        .around(mockWebServer)
        .around(getKoinRule())
        .around(activityRule)

    override fun apply(base: Statement?, description: Description?): Statement {
        return delegateRule.apply(base, description)
    }

    private fun getKoinRule(): StartAndEndKoinRule {
        val baseUrl = mockWebServer.url("/").toString()
        return StartAndEndKoinRule(baseUrl)
    }

    inner class StartAndEndKoinRule(
        private val baseUrl: String
    ) : ExternalResource() {

        override fun before() {
            super.before()
            stopKoin()
            startKoin(listOf(networkModule,
                viewModelModule,
                repositoryModule,
                sharedPreferencesModule
            ), KoinProperties(
                false,
                false,
                extraProperties = mapOf(PROPERTY_BASE_URL to baseUrl))
            )

        }

        override fun after() {
            stopKoin()
            mockWebServer.shutdown()
            super.after()
        }
    }
}