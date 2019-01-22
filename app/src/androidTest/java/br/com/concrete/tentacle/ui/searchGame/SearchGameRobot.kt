package br.com.concrete.tentacle.ui.searchGame

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseTestRobot

fun search(func: SearchGameRobot.()-> Unit) = SearchGameRobot().apply {
    func()
}

class SearchGameRobot: BaseTestRobot() {

    fun clickSearchButton() = actionClick(R.id.action_search)

    fun setSearchText(textSearch: String) = fillEditSearchText(textSearch)

    fun isDisplayedGame() = matchDisplayed("Uncharted 4")

    //fun isDisplayedView() =

}