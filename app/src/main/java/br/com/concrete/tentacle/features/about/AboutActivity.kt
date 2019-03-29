package br.com.concrete.tentacle.features.about

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.visible
import kotlinx.android.synthetic.main.activity_about.progress
import kotlinx.android.synthetic.main.activity_about.webView

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        init()
    }

    private fun init(){
        setupToolbar(R.string.toolbar_title_about, R.drawable.ic_close)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visible(false)
            }
        }
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        webView.loadUrl("https://github.com/ftrevo/tentacle-android/wiki/Sobre")
    }

    override fun getFinishActivityTransition() = ActivityAnimation.TRANSLATE_DOWN
}