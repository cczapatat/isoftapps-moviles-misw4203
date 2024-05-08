package miso4203.mobile.app.vinilos.models

data class ArtistDetail(
    override val id: Int,
    override val name: String,
    override val image: String,
    val description: String,
    val albums: ArrayList<Album>,
) : Artist(id, name, image, albums.size)
