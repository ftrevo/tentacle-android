package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.data.models.library.loan.LoanResponse

class LoansListResponse(
    val list: List<LoanResponse>,
    val count: Int
)