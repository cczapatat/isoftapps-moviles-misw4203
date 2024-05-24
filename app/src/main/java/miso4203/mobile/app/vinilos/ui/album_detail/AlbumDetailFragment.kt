package miso4203.mobile.app.vinilos.ui.album_detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import miso4203.mobile.app.vinilos.cache.PicassoWrapper
import miso4203.mobile.app.vinilos.databinding.FragmentAlbumDetailBinding
import miso4203.mobile.app.vinilos.ui.adapters.PerformerAdapter
import miso4203.mobile.app.vinilos.ui.adapters.TrackAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        binding.btnBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }

        val args: AlbumDetailFragmentArgs by navArgs()
        viewModel = ViewModelProvider(
            this, AlbumDetailViewModel.Factory(activity.application, args.albumId)
        )[AlbumDetailViewModel::class.java]

        viewModel.getData()

        viewModel.album.observe(viewLifecycleOwner) {
            it.apply {
                try {
                    PicassoWrapper.getInstance(binding.root.context).load(it.cover)
                        .into(binding.albumDetailImage)
                } catch (_: Exception) {
                }
                val dateTime = try {
                    // Define the format of the date string
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    // Parse the date string to LocalDateTime
                    LocalDateTime.parse(it.releaseDate, formatter)
                } catch (_: Exception) {
                    LocalDateTime.now()
                }
                binding.textProfile.text = it.name
                binding.albumDetailName.text =
                    it.name + ":" + it.description + "\n" + "The album was released on " + dateTime.month.name.lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + " " + dateTime.dayOfMonth.toString() + ", " + dateTime.year.toString()
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
            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }

        sharedPreferences = activity.getSharedPreferences("CLL_APP", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isCollector", false)) {
            binding.btnGoToAddTrackFromAlbum.setOnClickListener {
                android.os.Handler().postDelayed({
                    binding.root.findNavController().navigate(
                        AlbumDetailFragmentDirections.actionAlbumDetailFragmentToTrackAddFragment(
                            args.albumId, -1
                        )
                    )
                }, 100)
            }

            binding.btnGoToAddTrackFromAlbum.visibility = View.VISIBLE
        } else {
            binding.btnGoToAddTrackFromAlbum.visibility = View.INVISIBLE
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