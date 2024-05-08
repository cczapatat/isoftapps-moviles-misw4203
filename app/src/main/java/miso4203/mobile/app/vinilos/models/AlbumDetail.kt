package miso4203.mobile.app.vinilos.models

data class AlbumDetail(
    override val id: Int,
    override val name: String,
    override val cover: String,
    override val releaseDate: String,
    override val description: String,
    override val genre: String,
    val tracks: ArrayList<Track>,
    val performers: ArrayList<Performer>,
) : Album(id, name, cover, releaseDate, description, genre)

