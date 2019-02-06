package br.com.concrete.tentacle.base

import android.graphics.Bitmap
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import java.io.IOException

class ScreenshotTestRule : TestWatcher() {

    override fun failed(e: Throwable?, description: org.junit.runner.Description?) {
        super.failed(e, description)

        takeScreenShot(description)
    }

    private fun takeScreenShot(description: org.junit.runner.Description?) {
        description?.let { desc ->
            val fileName = "${desc.testClass.simpleName} - $desc"

            val capture = Screenshot.capture()
            capture.name = fileName
            capture.format = Bitmap.CompressFormat.PNG
            val processors = HashSet<ScreenCaptureProcessor>()
            processors.add(BasicScreenCaptureProcessor())

            try {
                capture.process(processors)
            }catch (ex: IOException){
                ex.printStackTrace()
            }
        }
    }
}