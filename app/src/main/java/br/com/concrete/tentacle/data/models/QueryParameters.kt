package br.com.concrete.tentacle.data.models

import android.text.TextUtils

data class QueryParameters(
    var id: String? = null,
    var name: String? = null,
    var status: String? = null,
    var mediaOwner: String? = null,
    var mediaId: String? = null,
    var mediaPlatform: ArrayList<String>? = null,
    var limit: Int? = 80, // FIXME: setting to 80 while pagination is not finished
    var page: Int? = 0 // First page as default page
) {
    override fun toString(): String {
        return if (mediaPlatform != null) TextUtils.join(",", mediaPlatform) else ""
    }
}