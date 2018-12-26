package br.com.concrete.tentacle.features.register

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import br.com.concrete.tentacle.MainActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.digits
import br.com.concrete.tentacle.resError
import kotlinx.android.synthetic.main.register_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.StringBuilder


class RegisterFragment : Fragment() {

    private var isPhoneValid = false
    private val viewModelRegister: RegisterUserViewModel by viewModel()
    private lateinit var states: ArrayList<State>
    private lateinit var cities: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initPhoneValidate()
    }

    private fun init() {

        viewModelRegister.getStates().observe(this, Observer {
            states = it as ArrayList<State>

            spState.adapter = ArrayAdapter<State>(context!!, R.layout.spinner_item_layout, states)

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

        viewModelRegister.getCities().observe(this, Observer {
            cities = it as ArrayList<String>
            spCity.adapter = ArrayAdapter<String>(context!!, R.layout.spinner_item_layout, cities)
        })

        viewModelRegister.getUser().observe(this, Observer {
            //TODO REFACTOR TO EXACT VIEW
            val mainActivity = Intent(activity, MainActivity::class.java)
            mainActivity.putExtra(MainActivity.USER, it)
            startActivity(mainActivity)
            fragmentManager!!.beginTransaction().remove(this).commit()
        })

        viewModelRegister.getError().observe(this, Observer { errors ->

            var message = String()
            errors.forEach{ eachMessage ->
                message += ("\n" + eachMessage)
            }

            val alertDialog: AlertDialog? = activity?.let { fragment ->
                val builder = AlertDialog.Builder(fragment)
                builder.setTitle(R.string.error_dialog_title)
                builder.setMessage(message)
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
        })

        btnCreateAccount.setOnClickListener {
            performValidation()
        }
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

        if (!edtEmail.text.toString().isValidEmail()) {
            tilEmail.resError(R.string.email_error)
            result = false
        }

        if (result && edtUserName.text.toString().isBlank()) {
            tilUserName.resError(R.string.name_error)
            result = false
        }

        if (result && edtPassword.text.toString().length < 6) {
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


    //TODO Refactor this block after merge
    fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()


}