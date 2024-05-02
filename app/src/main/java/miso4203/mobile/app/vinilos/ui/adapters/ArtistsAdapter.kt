package miso4203.mobile.app.vinilos.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.ArtistItemBinding
import miso4203.mobile.app.vinilos.models.Artist


class ArtistsAdapter() :
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

            Picasso.get().load(artists[position].image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
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