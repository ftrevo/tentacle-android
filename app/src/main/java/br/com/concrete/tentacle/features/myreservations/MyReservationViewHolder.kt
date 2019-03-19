package br.com.concrete.tentacle.features.myreservations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_COVER_SMALL
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_my_reservation.view.dateImage
import kotlinx.android.synthetic.main.item_my_reservation.view.game_name
import kotlinx.android.synthetic.main.item_my_reservation.view.game_owner
import kotlinx.android.synthetic.main.item_my_reservation.view.mediaImageView
import kotlinx.android.synthetic.main.item_my_reservation.view.status

class MyReservationViewHolder(
    private val layout: View
) : RecyclerView.ViewHolder(layout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, loanResponse: LoanResponse, loanClick: (LoanResponse) -> Unit) {
            if (holder is MyReservationViewHolder) {
                loanResponse.game.cover?.imageId?.let { imageId ->
                    holder.layout.mediaImageView.loadImageUrl(
                        Utils.assembleGameImageUrl(
                            IMAGE_SIZE_TYPE_COVER_SMALL,
                            imageId
                        )
                    )
                }

                holder.layout.game_owner.text = String.format(holder.itemView.context.getString(R.string.my_reservation_owner_name), loanResponse.mediaOwner.name)
                holder.layout.game_name.text = loanResponse.game.name

                var text: String? = null
                var visibility: Int? = null
                when (loanResponse.getLoanState()) {
                    LoanResponse.LoanState.ACTIVE -> {
                        text = loanResponse.estimatedReturnDate?.toDate()?.format(SIMPLE_DATE_OUTPUT_FORMAT) ?: ""
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
                loanClick(loanResponse)
            }
        }
    }
}