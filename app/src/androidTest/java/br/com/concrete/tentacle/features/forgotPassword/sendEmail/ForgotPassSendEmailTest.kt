package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPassSendEmailTest: BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<ForgotPassSendEmailActivity>(ForgotPassSendEmailActivity::class.java) {}




}