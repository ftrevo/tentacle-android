package br.com.concrete.tentacle.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.navigateTo(container: Int, destination: Fragment){
    supportFragmentManager.beginTransaction().replace(container, destination).commit()
}