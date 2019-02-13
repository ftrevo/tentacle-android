package br.com.concrete.tentacle.data.models

data class QueryParameters(
    var id: String? = null,
    var title: String? = null,
    var status: String? = null,
    var mediaOwner: String? = null,
    var mediaId: String? = null,
    var mediaPlatform: ArrayList<String>? = null,
    var limit: Int? = 80, // FIXME: setting to 80 while pagination is not finished
    var page: Int? = 0 // First page as default page
)