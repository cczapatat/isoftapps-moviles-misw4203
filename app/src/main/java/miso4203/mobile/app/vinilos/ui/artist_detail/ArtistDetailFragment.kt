package miso4203.mobile.app.vinilos.ui.artist_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import miso4203.mobile.app.vinilos.cache.PicassoWrapper
import miso4203.mobile.app.vinilos.databinding.FragmentArtistDetailBinding


class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var viewModel: ArtistDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)

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

        val args: ArtistDetailFragmentArgs by navArgs()
        viewModel = ViewModelProvider(
            this,
            ArtistDetailViewModel.Factory(activity.application, args.artistId)
        )[ArtistDetailViewModel::class.java]

        viewModel.artistDetail.observe(viewLifecycleOwner) {
            it.apply {
                try {
                    PicassoWrapper.getInstance(binding.root.context)
                        .load(it.image)
                        .into(binding.artistDetailImage)
                } catch (_: Exception) {
                }

                binding.textProfile.text = it.name
                binding.artistDetailName.text = it.name
                binding.artistDetailDescription.text = it.description
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