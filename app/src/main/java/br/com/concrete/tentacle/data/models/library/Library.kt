package br.com.concrete.tentacle.data.models.library

import br.com.concrete.tentacle.data.models.Media

data class Library(
    var _id: String? = null,
    var title: String? = null,
    var media: Media? = null
)