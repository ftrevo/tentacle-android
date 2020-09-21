package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun arrange(action: ForgotPassSendEmailArrange.() -> Unit) {
    ForgotPassSendEmailArrange().apply(action)
}

fun act(action: ForgotPassSendEmailAct.() -> Unit) {
    ForgotPassSendEmailAct().apply(action)
}

fun assert(action: ForgotPassSendEmailAssert.() -> Unit) {
    ForgotPassSendEmailAssert().apply(action)
}

class ForgotPassSendEmailArrange {

    fun mockSuccess(mockWebServer: MockWebServer){
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/forgotPassword/sendEmail/user.json".getJson())
        )
    }

    fun mockError(mockWebServer: MockWebServer){
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
    }

}

class ForgotPassSendEmailAct {



}

class ForgotPassSendEmailAssert {

}