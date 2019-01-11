package br.com.concrete.tentacle.features.loadmygames

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

open class GameViewHolder(
    val mLinearLayout: LinearLayout
): RecyclerView.ViewHolder(mLinearLayout){

    fun callBack(holder: GameViewHolder, position: Int) {
        holder.mLinearLayout
    }

    fun createHolder(mLinearLayout: LinearLayout) : RecyclerView.ViewHolder{
        return GameViewHolder(mLinearLayout)
    }

}