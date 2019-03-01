package br.com.concrete.tentacle.features.myreservations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.item_my_reservation.view.dateImage
import kotlinx.android.synthetic.main.item_my_reservation.view.game_name
import kotlinx.android.synthetic.main.item_my_reservation.view.game_owner
import kotlinx.android.synthetic.main.item_my_reservation.view.status

class MyReservationViewHolder(
    private val layout: View
) : RecyclerView.ViewHolder(layout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, el: LoanResponse, loanClick: (LoanResponse) -> Unit) {
            if (holder is MyReservationViewHolder) {
                holder.layout.game_owner.text = String.format(holder.itemView.context.getString(R.string.my_reservation_owner_name), el.mediaOwner.name)
                holder.layout.game_name.text = el.game.name

                var text: String? = null
                var visibility: Int? = null
                when (el.getLoanState()) {
                    LoanResponse.LoanState.ACTIVE -> {
                        text = el.loanDate?.toDate()?.format(SIMPLE_DATE_OUTPUT_FORMAT) ?: ""
                        visibility = View.VISIBLE
                    }
                    LoanResponse.LoanState.PENDING -> {
                        text = holder.itemView.context.getString(R.string.loan_state_pending)
                        visibility = View.GONE
                    }
                    LoanResponse.LoanState.EXPIRED -> {
                        text = holder.itemView.context.getString(R.string.loan_state_expired)
                        visibility = View.GONE
                    }
                }

                text?.let {
                    holder.layout.status.text = text
                }
                visibility?.let {
                    holder.layout.dateImage.visibility = visibility
                }
                loanClick(el)
            }
        }
    }
}