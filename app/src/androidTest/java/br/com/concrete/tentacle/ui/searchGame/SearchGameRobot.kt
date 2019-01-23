package br.com.concrete.tentacle.ui.searchGame

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseTestRobot

fun search(func: SearchGameRobot.()-> Unit) = SearchGameRobot().apply {
    func()
}

class SearchGameRobot: BaseTestRobot() {

    fun clickSearchButton() = actionClick(R.id.action_search)

    fun setSearchText(textSearch: String) = fillEditSearchText(textSearch)

    fun isDisplayedGame() = matchDisplayed("Fifa 2019")

    fun isDisplayedProgress() = matchDisplayed(R.id.progressBarList)

    fun isDisplayedRecyclerView() = matchDisplayedRecyclerView(R.id.recyclerListView, "FIFA 2019")

    //fun isDisplayedView() =

}