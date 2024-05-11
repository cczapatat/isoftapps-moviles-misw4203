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
import miso4203.mobile.app.vinilos.databinding.ArtistItemBinding
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.ui.artist.ArtistFragmentDirections


class ArtistsAdapter :
    RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    var artists: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), ArtistViewHolder.LAYOUT, parent, false
        )

        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.viewDataBinding.also {
            holder.viewDataBinding.artist = artists[position]

            PicassoWrapper.getInstance(holder.itemView.context)
                .load(artists[position].image)
                .into(it.artistImage)

            holder.viewDataBinding.root.setOnClickListener {
                holder.viewDataBinding.cardViewArtist.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.selected_item_color
                    )
                )
                android.os.Handler().postDelayed(
                    {
                        holder.viewDataBinding.cardViewArtist.setBackgroundColor(Color.TRANSPARENT)
                        holder.viewDataBinding.root.findNavController().navigate(
                            ArtistFragmentDirections.navigateToArtistDetail(artists[position].id)
                        )
                    }, 150
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item
        }
    }
}