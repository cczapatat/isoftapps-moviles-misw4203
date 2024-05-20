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
import miso4203.mobile.app.vinilos.databinding.CollectorItemBinding
import miso4203.mobile.app.vinilos.models.Collector
import miso4203.mobile.app.vinilos.ui.collector.CollectorFragmentDirections

class CollectorsAdapter : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {

    var collectors: List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorsAdapter.CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), CollectorsAdapter.CollectorViewHolder.LAYOUT, parent, false
        )

        return CollectorsAdapter.CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorsAdapter.CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            holder.viewDataBinding.collector = collectors[position]

            if (collectors[position].favoritePerformers[0] != null){
                PicassoWrapper.getInstance(holder.itemView.context)
                    .load(collectors[position].favoritePerformers[0].image)
                    .into(it.collectorCover)
            }


            holder.viewDataBinding.root.setOnClickListener {
                holder.viewDataBinding.cardViewCollector.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.selected_item_color
                    )
                )
                android.os.Handler().postDelayed(
                    {
                        holder.viewDataBinding.cardViewCollector.setBackgroundColor(Color.TRANSPARENT)
                        holder.viewDataBinding.root.findNavController().navigate(
                            CollectorFragmentDirections.navigateToCollectorDetail(collectors[position].id)
                        )
                    }, 150
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }
    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }
    }
}