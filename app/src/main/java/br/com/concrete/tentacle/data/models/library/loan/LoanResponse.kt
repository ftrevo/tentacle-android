package br.com.concrete.tentacle.data.models.library.loan

import br.com.concrete.tentacle.data.models.Game

data class LoanResponse(
    val _id: String,
    val game: Game,
    val media: Media,
    val mediaOwner: MediaOwner,
    val requestedAt: String,
    val requestedBy: RequestedBy
)