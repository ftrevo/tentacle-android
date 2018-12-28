package br.com.concrete.tentacle.features.register

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.canarinho.watcher.TelefoneTextWatcher
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacao
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.digits
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.extensions.validatePassword
import br.com.concrete.tentacle.features.MainActivity
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private var isPhoneValid = false
    private val viewModelRegister: RegisterUserViewModel by viewModel()
    private var states: ArrayList<State>? = null
    private var cities: ArrayList<String>? = null

    private lateinit var dialogState: SpinnerDialog
    private lateinit var dialogCity: SpinnerDialog

    private var stateSelected: State? = null
    private var citySelected: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initPhoneValidate()
    }

    private fun init() {

        viewModelRegister.getStates().observe(this, Observer { viewState ->

            when (viewState.status){
                ViewStateModel.Status.LOADING -> {
                    progressButton(true)
                    enableField(false)
                }
                ViewStateModel.Status.SUCCESS -> {
                    states = viewState.model as ArrayList<State>
                    val statesList = states?.map{
                        it.initials
                    }
                    dialogState = SpinnerDialog(activity!!, statesList as ArrayList<String>,
                        getString(R.string.state_dialog_text), getString(R.string.dialog_close))
                    initDialogStateBind()
                    progressButton(false)
                    enableField(true)
                }
                ViewStateModel.Status.ERROR -> {
                    showError(viewState.errors!!.toString())
                    progressButton(false)
                    enableField(true)
                }
            }

            spState.setOnClickListener {
                states?.let{
                    dialogState.showSpinerDialog()
                }
            }

            spCity.setOnClickListener {
                cities?.let {
                    dialogCity.showSpinerDialog()
                }
            }
        })

        viewModelRegister.getCities().observe(this, Observer { viewState ->

            when(viewState.status){
                ViewStateModel.Status.LOADING -> {
                    progressButton(true)
                    enableField(false)
                }
                ViewStateModel.Status.SUCCESS -> {
                    cities = viewState.model as ArrayList<String>
                    dialogCity = SpinnerDialog(activity!!, cities, getString(R.string.city_dialog_text),
                        getString(R.string.dialog_close))
                    initDialogCityBind()
                    progressButton(false)
                }
                ViewStateModel.Status.ERROR -> {
                    showError(viewState.errors!!.message.toString())
                    progressButton(false)
                    enableField(true)
                }
            }
        })

        viewModelRegister.getUser().observe(this, Observer { viewState ->
            //TODO REFACTOR TO EXACT VIEW
            when(viewState.status){
                ViewStateModel.Status.LOADING -> {
                    progressButton(true)
                    enableField(false)
                }
                ViewStateModel.Status.SUCCESS -> {
                    progressButton(false)
                    enableField(true)
                    val mainActivity = Intent(activity, MainActivity::class.java)
                    mainActivity.putExtra(MainActivity.USER, viewState.model)
                    startActivity(mainActivity)
                    fragmentManager!!.beginTransaction().remove(this).commit()
                }
                ViewStateModel.Status.ERROR -> {
                    showError(viewState.errors!!.message.toString())
                    progressButton(false)
                    enableField(true)
                }
            }
        })

        btnCreateAccount.setOnClickListener {
            performValidation()
        }
    }

    private fun enableField(enable: Boolean) {
        edtEmail.isEnabled = enable
        edtUserName.isEnabled = enable
        edtPassword.isEnabled = enable
        edtPhone.isEnabled = enable
    }

    private fun showError(errors: String){

        val alertDialog: AlertDialog? = activity?.let { fragment ->
            val builder = AlertDialog.Builder(fragment)
            builder.setTitle(R.string.error_dialog_title)
            builder.setMessage(errors)
            builder.apply {
                setPositiveButton(R.string.ok
                ) { dialog, id ->
                    dialog.dismiss()
                }
            }

            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()
    }

    private fun initPhoneValidate() {
        edtPhone.edt.addTextChangedListener(TelefoneTextWatcher(object : EventoDeValidacao {
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
        edtEmail.showError(false)
        edtUserName.showError(false)
        edtPassword.showError(false)
        edtPhone.showError(false)
        spState.showError(false)
        spCity.showError(false)
    }

    private fun performValidation() {
        var result = true
        clearErrors()

        if (!edtEmail.getText().validateEmail()) {
            edtEmail.showError(true)
            result = false
        }

        if (result && edtUserName.getText().isBlank()) {
            edtUserName.showError(true)
            result = false
        }

        if (result && !edtPassword.getText().validatePassword()) {
            edtPassword.showError(true)
            result = false
        }

        if (result && !isPhoneValid) {
            edtPhone.showError(true)
            result = false
        }

        if (result && stateSelected == null ) {
            spState.showError(true)
            result = false
        }

        if (result && citySelected == null) {
            spCity.showError(true)
            result = false
        }

        if (result) {
            val user = User(
                email = edtEmail.getText(),
                name = edtUserName.getText(),
                password = edtPassword.getText(),
                phone = edtPhone.getText().digits(),
                state = stateSelected!!,
                city = citySelected!!
            )

            viewModelRegister.registerUser(user)
        }
    }

    private fun initDialogStateBind(){
        dialogState.bindOnSpinerListener { _, position ->
            stateSelected = states!![position]
            stateSelected?.let { state->
                viewModelRegister.loadCities(state._id)
                spState.setText(state.initials)
            }
        }
    }

    private fun initDialogCityBind() {
        dialogCity.bindOnSpinerListener { _, position ->
            citySelected = cities!![position]
            spCity.setText(citySelected!!)
        }
    }
    private fun progressButton(enable: Boolean) {
        btnCreateAccount?.isLoading(enable)
    }
}