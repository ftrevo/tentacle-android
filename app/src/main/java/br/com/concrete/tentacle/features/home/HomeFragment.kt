package br.com.concrete.tentacle.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}