package miso4203.mobile.app.vinilos.ui.collector_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import miso4203.mobile.app.vinilos.databinding.FragmentCollectorDetailBinding

class CollectorDetailFragment: Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectorViewModel =
            ViewModelProvider(this)[CollectorDetailViewModel::class.java]

        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textNotifications
        collectorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        _binding?.btnCreateAlbum?.setOnClickListener {
            binding.root.findNavController().navigate(
                CollectorDetailFragmentDirections.actionCollectorDetailFragmentToAlbumCreateFragment()
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}