package miso4203.mobile.app.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.PerformerItemBinding
import miso4203.mobile.app.vinilos.models.Performer
import miso4203.mobile.app.vinilos.ui.album_detail.AlbumDetailFragmentDirections

class PerformerAdapter(private val dataSet: ArrayList<Performer>) :
    RecyclerView.Adapter<PerformerAdapter.PerformerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val withDataBinding: PerformerItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), PerformerViewHolder.LAYOUT, parent, false
        )

        return PerformerViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.viewDataBinding.also {
            holder.viewDataBinding.performer = dataSet[position]
            holder.viewDataBinding.btnArtistDetail.setOnClickListener {
                android.os.Handler().postDelayed({
                    holder.viewDataBinding.root.findNavController().navigate(
                        AlbumDetailFragmentDirections.navigateToArtistFromAlbum(dataSet[position].id)
                    )
                }, 100)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class PerformerViewHolder(val viewDataBinding: PerformerItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.performer_item
        }
    }
}