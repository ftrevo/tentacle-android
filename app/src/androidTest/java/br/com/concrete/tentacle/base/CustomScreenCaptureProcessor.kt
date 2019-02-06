package br.com.concrete.tentacle.base

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import java.io.File

private val file = File(InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "espresso_screenshots")

class CustomScreenCaptureProcessor : BasicScreenCaptureProcessor(){
    init {

    }
}