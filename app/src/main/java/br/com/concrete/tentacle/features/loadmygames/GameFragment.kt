package br.com.concrete.tentacle.features.loadmygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import kotlinx.android.synthetic.main.fragment_game_list.view.*
import kotlinx.android.synthetic.main.list_custom.view.*

class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_game_list, container, false)

        v.list.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        v.list.recyclerListView.layoutManager = layoutManager

        val user = User("", "", "", "", "", State("", "", ""), "")
        val game = Game("", "FIFA 2019", user, "", "")
        var medias = ArrayList<Media>()

        populate(medias, user, game)

        val recyclerViewAdapter = BaseAdapter<Media>(
            medias,
            R.layout.fragment_game_item,
            {
                GameViewHolder(it)
            }, { holder, element ->
                callBack(holder = holder, element = element)
            })

        v.list.recyclerListView.adapter = recyclerViewAdapter

        v.list.updateUi(medias)
        return v
    }

    private fun populate(medias: ArrayList<Media>, user: User, game: Game) {
        medias.add(
            Media(_id = "fhd", platform = "PS3", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS4", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoDS", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoSwitch", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS3", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS4", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoDS", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoSwitch", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS3", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS4", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoDS", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoSwitch", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS3", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS4", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoDS", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoSwitch", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS3", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "PS4", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoDS", updatedAt = "", createdAt = "", owner = user, game = game)
        )
        medias.add(
            Media(_id = "fhd", platform = "NintendoSwitch", updatedAt = "", createdAt = "", owner = user, game = game)
        )
    }
}
