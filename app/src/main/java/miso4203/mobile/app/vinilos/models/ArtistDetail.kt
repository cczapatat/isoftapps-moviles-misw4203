package miso4203.mobile.app.vinilos.models

data class ArtistDetail(
    override val id: Int,
    override val name: String,
    override val image: String,
    override val description: String,
    override val birthDate: String,
    val albums: ArrayList<Album>,
) : Artist(id, name, image, description, birthDate, albums.size)
