package br.com.concrete.tentacle.features.profile

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.canarinho.watcher.TelefoneTextWatcher
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacao
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.extensions.digits
import br.com.concrete.tentacle.extensions.validateEmail
import kotlinx.android.synthetic.main.fragment_profile.citySearchSpinner
import kotlinx.android.synthetic.main.fragment_profile.emailEditText
import kotlinx.android.synthetic.main.fragment_profile.loadError
import kotlinx.android.synthetic.main.fragment_profile.phoneEditText
import kotlinx.android.synthetic.main.fragment_profile.saveChangesButton
import kotlinx.android.synthetic.main.fragment_profile.stateSearchSpinner
import kotlinx.android.synthetic.main.fragment_profile.userNameEditText
import kotlinx.android.synthetic.main.progress_include.progressBarList
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.edt
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment: BaseFragment() {

    private var isPhoneValid = false
    private var states: ArrayList<State>? = null
    private var cities: ArrayList<String>? = null

    private lateinit var dialogState: SpinnerDialog
    private lateinit var dialogCity: SpinnerDialog

    private var stateSelected: State? = null
    private var citySelected: String? = null

    private lateinit var currentUser: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservables()
        initButtonsClickListener()
        initPhoneValidate()
    }

    private fun initObservables() {
        //TODO - add observables
    }

    private fun setupViews() {
        with(currentUser) {
            userNameEditText.setText(name)
            emailEditText.setText(email)
            phoneEditText.setText(phone)

            stateSelected = state
            citySelected = city
        }
    }

    private fun initButtonsClickListener() {
        saveChangesButton.setOnClickListener {
            performValidation()
        }

        loadError.setUpActionErrorButton {
            //TODO - getProfile
        }

        stateSearchSpinner.setOnClickListener {
            states?.let {
                dialogState.showSpinerDialog()
            }
        }

        citySearchSpinner.setOnClickListener {
            cities?.let {
                dialogCity.showSpinerDialog()
            }
        }
    }

    private fun enableField(enable: Boolean) {
        userNameEditText.isEnabled = enable
        emailEditText.isEnabled = enable
        phoneEditText.isEnabled = enable
    }

    private fun initPhoneValidate() {
        phoneEditText.edt.addTextChangedListener(TelefoneTextWatcher(object : EventoDeValidacao {
            override fun totalmenteValido(valorAtual: String?) {
                isPhoneValid = true
            }

            override fun invalido(valorAtual: String?, mensagem: String?) {
                isPhoneValid = false
            }

            override fun parcialmenteValido(valorAtual: String?) {
                isPhoneValid = false
            }
        }))
    }

    private fun clearErrors() {
        userNameEditText.showError(false)
        emailEditText.showError(false)
        phoneEditText.showError(false)
        stateSearchSpinner.showError(false)
        citySearchSpinner.showError(false)
    }

    private fun initDialogStateBind() {
        dialogState.bindOnSpinerListener { _, position ->
            if (states != null) {
                stateSelected = states!![position]
                stateSelected?.let { state ->
                    //TODO - load cities
                    stateSearchSpinner.setText(state.initials)
                    resetCity()
                }
            }
        }
    }

    private fun resetCity() {
        citySearchSpinner.resetValue()
        citySelected = null
    }

    private fun initDialogCityBind() {
        dialogCity.bindOnSpinerListener { _, position ->
            if (cities != null) {
                citySelected = cities!![position]
                citySearchSpinner.setText(citySelected!!)
            }
        }
    }

    private fun performValidation() {
        var result = true
        clearErrors()

        if (result && userNameEditText.getText().isBlank()) {
            userNameEditText.showError(true)
            result = false
        }

        if (!emailEditText.getText().validateEmail()) {
            emailEditText.showError(true)
            result = false
        }

        if (result && !isPhoneValid) {
            phoneEditText.showError(true)
            result = false
        }

        if (result && stateSelected == null) {
            stateSearchSpinner.showError(true)
            result = false
        }

        if (result && citySelected == null) {
            citySearchSpinner.showError(true)
            result = false
        }

        if (result) {
            val userRequest = UserRequest(
                email = emailEditText.getText().trim(),
                name = userNameEditText.getText().trim(),
                phone = phoneEditText.getText().trim().digits(),
                state = stateSelected!!._id,
                city = citySelected!!,
                password = null
            )

            //TODO - update profile
        }
    }

    private fun progressButton(enable: Boolean) {
        saveChangesButton.isLoading(enable)
    }

    private fun showProgress(show: Boolean) {
        progressBarList.visibility = setVisibility(show)
    }

    private fun showLoadError(show: Boolean) {
        loadError.visibility = setVisibility(show)
    }

    private fun setVisibility(isVisible: Boolean) = if (isVisible) VISIBLE else GONE

    override fun getToolbarTitle(): Int = R.string.profile_title

}