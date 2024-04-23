package miso4203.mobile.app.vinilos.ui.album_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import miso4203.mobile.app.vinilos.databinding.FragmentAlbumDetailBinding
import miso4203.mobile.app.vinilos.ui.adapters.PerformerAdapter
import miso4203.mobile.app.vinilos.ui.adapters.TrackAdapter


class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var viewModel: AlbumDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val args: AlbumDetailFragmentArgs by navArgs()
        viewModel = ViewModelProvider(
            this,
            AlbumDetailViewModel.Factory(activity.application, args.albumId)
        )[AlbumDetailViewModel::class.java]

        viewModel.album.observe(viewLifecycleOwner) {
            it.apply {
                try {
                    Picasso.get().load(it.cover)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(binding.albumDetailImage)
                } catch (ex: Exception) {}
                binding.albumDetailName.text = it.name + ":"+ it.description
                binding.albumDetailPerformersTitle.text = "Performers:"
                binding.albumDetailTracksTitle.text = "Tracks:"

                // Load performer data
                val recyclerView: RecyclerView = binding.performerDetailRv
                val manager = LinearLayoutManager(context)
                recyclerView.layoutManager = manager
                val performerAdapter = PerformerAdapter(it.performers)
                recyclerView.adapter = performerAdapter

                // Load track data
                val recyclerViewTrack: RecyclerView = binding.trackDetailRv
                val managerTrack = LinearLayoutManager(context)
                recyclerViewTrack.layoutManager = managerTrack
                val trackAdapter = TrackAdapter(it.tracks)
                recyclerViewTrack.adapter = trackAdapter

                it.releaseDate
            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}