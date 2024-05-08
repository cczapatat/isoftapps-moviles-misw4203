package miso4203.mobile.app.vinilos.models

open class Album(
    open val id: Int = 0,
    open val name: String,
    open val cover: String,
    open val releaseDate: String = "",
    open val description: String = "",
    open val genre: String,
    val recordLabel: String = ""
)