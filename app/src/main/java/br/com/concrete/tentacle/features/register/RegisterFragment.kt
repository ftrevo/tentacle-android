package br.com.concrete.tentacle.features.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import br.com.concrete.canarinho.watcher.TelefoneTextWatcher
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacao
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.resError
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment: Fragment() {

    private var isPhoneValid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initPhoneValidate()
    }

    private fun init(){

        //TODO Replace to API
        val states = resources.getStringArray(R.array.estados)
        spState.adapter = ArrayAdapter<String>(context!!, R.layout.spinner_item_layout, states)

        val cities = resources.getStringArray(R.array.cidades)
        spCity.adapter = ArrayAdapter<String>(context!!, R.layout.spinner_item_layout, cities)

        btnCreateAccount.setOnClickListener {
            performValidation()
        }
    }

    private fun initPhoneValidate(){
        edtPhone.addTextChangedListener(TelefoneTextWatcher(object: EventoDeValidacao {
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

    private fun clearErrors(){
        tilEmail.error = null
        tilUserName.error = null
        tilPassword.error = null
        tilPhone.error = null
        spState.error = null
        spCity.error = null
    }
    
    private fun performValidation(){
        var result = true
        clearErrors()

        if(!edtEmail.text.toString().isValidEmail()){
            tilEmail.resError(R.string.email_error)
            result = false
        }

        if(result && edtUserName.text.toString().isBlank()){
            tilUserName.resError(R.string.name_error)
            result = false
        }

        if(result && edtPassword.text.toString().length < 6){
            tilPassword.resError(R.string.password_error)
            result = false
        }

        if(result && !isPhoneValid){
            tilPhone.resError(R.string.phone_error)
            result = false
        }

        if(result && spState.selectedItemId == 0L){
            spState.setError(R.string.state_error)
            result = false
        }

        if(result && spCity.selectedItemId == 0L){
            spCity.setError(R.string.city_error)
            result = false
        }

        if(result){
            //TODO Create model and call API
        }
    }


    //TODO Refactor this block after merge
    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()


}