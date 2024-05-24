package miso4203.mobile.app.vinilos.ui.collector_detail

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
import miso4203.mobile.app.vinilos.databinding.FragmentCollectorDetailBinding
import miso4203.mobile.app.vinilos.ui.adapters.CollectorAlbumsAdapter

class CollectorDetailFragment : Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorDetailViewModel
    private var viewModelAdapter: CollectorAlbumsAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        viewModelAdapter = CollectorAlbumsAdapter()

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

        val args: CollectorDetailFragmentArgs by navArgs()

        viewModel = ViewModelProvider(
            this, CollectorDetailViewModel.Factory(activity.application, args.collectorId)
        )[CollectorDetailViewModel::class.java]

        viewModel.getData()

        viewModel.collectorAlbums.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.collector_albums = this

                binding.textCollectorAlbumGenre.text = it[0].album?.genre
                binding.collectorDetailName.text = it[0].collector?.name

                try {
                    PicassoWrapper.getInstance(binding.root.context)
                        .load(it[0].album?.cover)
                        .into(binding.collectorAlbumImage)
                } catch (_: Exception) {
                }
            }
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        sharedPreferences = activity.getSharedPreferences("CLL_APP", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isCollector", false)) {
            _binding?.btnCreateAlbum?.setOnClickListener {
                binding.root.findNavController().navigate(
                    CollectorDetailFragmentDirections.actionCollectorDetailFragmentToAlbumCreateFragment(args.collectorId)
                )
            }

            _binding?.btnAddTrack?.setOnClickListener {
                binding.root.findNavController().navigate(
                    CollectorDetailFragmentDirections.actionCollectorDetailFragmentToTrackAddFragment(
                        -1,args.collectorId
                    )
                )
            }

            binding.btnCreateAlbum.visibility = View.VISIBLE
            binding.btnAddTrack.visibility = View.VISIBLE
        } else {
            binding.btnCreateAlbum.visibility = View.INVISIBLE
            binding.btnAddTrack.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorAlbumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
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