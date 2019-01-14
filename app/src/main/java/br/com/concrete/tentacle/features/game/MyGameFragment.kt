package br.com.concrete.tentacle.features.game


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.searchGame.SearchActivity
import br.com.concrete.tentacle.features.searchGame.SearchGameFragment
import kotlinx.android.synthetic.main.fragment_my_game.*
import org.koin.android.ext.android.inject

class MyGameFragment : BaseFragment() {

    private val myGameViewModel: MyGameViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        addNovoJogo.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

    }

    private fun init() {
        myGameViewModel.getLiveGame().observe(this, Observer {base->
            when (base.status) {
                ViewStateModel.Status.LOADING -> {
                    //TODO exibir progress
                }
                ViewStateModel.Status.SUCCESS -> {
                    Log.i("SIZE LIST", base.model!!.size.toString())
                }
                ViewStateModel.Status.ERROR -> {
                    Log.i("ERROR MY GAME", base.errors.toString())
                }
            }
        })
        lifecycle.addObserver(myGameViewModel)
    }


}
