package miso4203.mobile.app.vinilos.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.cache.PicassoWrapper
import miso4203.mobile.app.vinilos.databinding.AlbumItemBinding
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.ui.album.AlbumFragmentDirections


class AlbumsAdapter() :
    RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), AlbumViewHolder.LAYOUT, parent, false
        )

        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            holder.viewDataBinding.album = albums[position]

            PicassoWrapper.getInstance(holder.itemView.context)
                .load(albums[position].cover)
                .into(it.albumCover)

            holder.viewDataBinding.root.setOnClickListener {
                holder.viewDataBinding.cardView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.selected_item_color
                    )
                )
                android.os.Handler().postDelayed(
                    {
                        holder.viewDataBinding.cardView.setBackgroundColor(Color.TRANSPARENT)
                        holder.viewDataBinding.root.findNavController().navigate(
                            AlbumFragmentDirections.navigateToAlbumDetail(albums[position].id)
                        )
                    }, 150
                )
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