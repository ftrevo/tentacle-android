package br.com.concrete.tentacle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.features.user.UserViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getStates().observe(this,  Observer { listStates ->
            listStates.forEach {state ->
                Log.d("STATE: ", state.name)
            }

            var user = User(email = "test.01@concrete.com.br", phone = "81 992586542")
            user.name = "Test Register"
            user.password = "password"
            user.setState(listStates[5])
            user.city = "CITY - ${listStates[5].initials}"

            viewModel.registerUser(user)

        })

//        viewModel.getUser().observe(this, Observer { user ->
//            Log.d("User: ", user.toString())
//
//            viewModel.loadCities(user.stateId)
//        })
//
//        viewModel.getCities().observe(this, Observer {cities ->
//            cities.forEach { city ->
//                Log.d("STATE: ", city)
//            }
//        })
//
//        viewModel.getError().observe(this, Observer {errors ->
//            errors.forEach { error ->
//                Log.e("STATE: ", error)
//            }
//        })
    }
}
