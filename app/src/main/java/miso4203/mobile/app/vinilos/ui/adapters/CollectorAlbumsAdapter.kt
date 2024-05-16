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
import miso4203.mobile.app.vinilos.databinding.CollectorAlbumItemBinding
import miso4203.mobile.app.vinilos.models.CollectorAlbum
import miso4203.mobile.app.vinilos.ui.collector.CollectorFragmentDirections

class CollectorAlbumsAdapter : RecyclerView.Adapter<CollectorAlbumsAdapter.CollectorAlbumViewHolder>() {

    var collector_albums: List<CollectorAlbum> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumsAdapter.CollectorAlbumViewHolder {
        val withDataBinding: CollectorAlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), CollectorAlbumsAdapter.CollectorAlbumViewHolder.LAYOUT, parent, false
        )

        return CollectorAlbumsAdapter.CollectorAlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumsAdapter.CollectorAlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            holder.viewDataBinding.collectorAlbum = collector_albums[position]

            if (collector_albums[position].album != null){
                PicassoWrapper.getInstance(holder.itemView.context)
                    .load(collector_albums[position].album?.cover)
                    .into(it.collectorAlbumCover)
            }


            holder.viewDataBinding.root.setOnClickListener {
                holder.viewDataBinding.cardViewCollectorAlbum.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.selected_item_color
                    )
                )
                android.os.Handler().postDelayed(
                    {
                        holder.viewDataBinding.cardViewCollectorAlbum.setBackgroundColor(Color.TRANSPARENT)
                        holder.viewDataBinding.root.findNavController().navigate(
                            CollectorFragmentDirections.navigateToCollectorDetail(collector_albums[position].id)
                        )
                    }, 150
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return collector_albums.size
    }
    class CollectorAlbumViewHolder(val viewDataBinding: CollectorAlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_album_item
        }
    }
}