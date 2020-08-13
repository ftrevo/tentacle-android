package br.com.concrete.tentacle.features.profile

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupToolbar(R.string.profile_title, R.drawable.arrow_back)
    }
}