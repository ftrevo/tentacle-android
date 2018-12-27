package br.com.concrete.tentacle.features.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
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
import br.com.concrete.tentacle.resError
import kotlinx.android.synthetic.main.register_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private var isPhoneValid = false
    private val viewModelRegister: RegisterUserViewModel by viewModel()
    private lateinit var states: ArrayList<State>
    private lateinit var cities: ArrayList<String>
    private lateinit var views: View

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
                    spState.adapter = ArrayAdapter<State>(context!!, R.layout.spinner_item_layout, states)
                    progressButton(false)
                    enableField(true)
                }
                ViewStateModel.Status.ERROR -> {
                    showError(viewState.errors!!.toString())
                    progressButton(false)
                    enableField(true)
                }
            }

            spState.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != -1) {
                        viewModelRegister.loadCities(states[position]._id)
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {

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
                    spCity.adapter = ArrayAdapter<String>(context!!, R.layout.spinner_item_layout, cities)
                    progressButton(false)
                    enableField(true)
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
        edtPhone.addTextChangedListener(TelefoneTextWatcher(object : EventoDeValidacao {
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
        tilEmail.error = null
        tilUserName.error = null
        tilPassword.error = null
        tilPhone.error = null
        spState.error = null
        spCity.error = null
    }

    private fun performValidation() {
        var result = true
        clearErrors()

        if (edtEmail.text.toString().validateEmail()) {
            tilEmail.resError(R.string.email_error)
            result = false
        }

        if (result && edtUserName.text.toString().isBlank()) {
            tilUserName.resError(R.string.name_error)
            result = false
        }

        if (result && edtPassword.text.toString().validatePassword()) {
            tilPassword.resError(R.string.password_error)
            result = false
        }

        if (result && !isPhoneValid) {
            tilPhone.resError(R.string.phone_error)
            result = false
        }

        if (result && spState.selectedItemId == 0L) {
            spState.setError(R.string.state_error)
            result = false
        }

        if (result && spCity.selectedItemId == 0L) {
            spCity.setError(R.string.city_error)
            result = false
        }

        if (result) {
            val user = User(
                email = edtEmail.text.toString(),
                name = edtUserName.text.toString(),
                password = edtPassword.text.toString(),
                phone = edtPhone.text.toString().digits(),
                state = states[spState.selectedItemPosition-1],
                city = cities[spCity.selectedItemPosition-1]
            )

            viewModelRegister.registerUser(user)
        }
    }

    private fun progressButton(enable: Boolean) {
        btnCreateAccount?.isLoading(enable)
    }
}