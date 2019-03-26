package br.com.concrete.tentacle.data.models.library.loan

data class LoanDeleteResponse (
    val _id: String,
    val media: String,
    val requestedBy: String,
    val mediaOwner: String,
    val game: String,
    val mediaPlatform: String,
    val requestedAt: String
)