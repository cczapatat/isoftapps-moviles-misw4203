package miso4203.mobile.app.vinilos.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.AlbumItemBinding
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.ui.album.AlbumViewModel


class AlbumsAdapter(private val albumViewModel: AlbumViewModel): RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    var albums :List<Album> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false)
        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val album  = albums[position]

            holder.viewDataBinding.album = album

            Picasso.get().load(albums[position].cover)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(it.albumCover)

            holder.viewDataBinding.root.setOnClickListener() {
                albumViewModel.setSelectedAlbumId(album.id)
                //TODO: Remover este toaster
                holder.viewDataBinding.cardView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selected_item_color))
                android.os.Handler().postDelayed(
                    {
                        holder.viewDataBinding.cardView.setBackgroundColor(Color.TRANSPARENT)
                    }, 185
                )

                Toast.makeText(holder.itemView.context, "Selected Album ID: ${album.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_item
        }
    }
}