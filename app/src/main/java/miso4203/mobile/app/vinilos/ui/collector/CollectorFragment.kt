package miso4203.mobile.app.vinilos.ui.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import miso4203.mobile.app.vinilos.databinding.FragmentCollectorBinding
import miso4203.mobile.app.vinilos.ui.album.AlbumFragmentDirections

class CollectorFragment : Fragment() {

    private var _binding: FragmentCollectorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectorViewModel =
            ViewModelProvider(this).get(CollectorViewModel::class.java)

        _binding = FragmentCollectorBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textNotifications
        collectorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        _binding?.btnCreateAlbum?.setOnClickListener {
            val action = CollectorFragmentDirections.actionCollectorFragmentToAlbumCreateFragment()
            binding.root.findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}