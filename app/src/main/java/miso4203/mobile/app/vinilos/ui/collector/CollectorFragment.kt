package miso4203.mobile.app.vinilos.ui.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.FragmentCollectorBinding
import miso4203.mobile.app.vinilos.ui.adapters.AlbumsAdapter
import miso4203.mobile.app.vinilos.ui.adapters.CollectorsAdapter
import miso4203.mobile.app.vinilos.ui.album.AlbumFragment
import miso4203.mobile.app.vinilos.ui.album.AlbumViewModel

class CollectorFragment : Fragment() {
    companion object {
        const val ACCEPTABLE_QUERY_STRING_LENGHT = 3
    }

    private var _binding: FragmentCollectorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private var viewModelAdapter: CollectorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val collectorViewModel =
            ViewModelProvider(this)[CollectorViewModel::class.java]*/

        _binding = FragmentCollectorBinding.inflate(inflater, container, false)

        /*val textView: TextView = binding.textNotifications
        collectorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
       /* _binding?.btnCreateAlbum?.setOnClickListener {
            binding.root.findNavController().navigate(
                CollectorFragmentDirections.actionCollectorFragmentToAlbumCreateFragment()
            )
        }*/
        viewModelAdapter = CollectorsAdapter()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val searchView = activity.findViewById<SearchView>(R.id.searchView)

        viewModel = ViewModelProvider(
            this, CollectorViewModel.Factory(activity.application)
        )[CollectorViewModel::class.java]

        viewModel.getData()

        viewModel.collectors.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.collectors = this
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                filterItems(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItems(newText)
                return false
            }

            private fun filterItems(query: String?) {
                if (query.isNullOrBlank()) {
                    viewModelAdapter?.collectors = viewModel.collectorsOrigin
                    return
                } else if (query.length >= CollectorFragment.ACCEPTABLE_QUERY_STRING_LENGHT) {
                    viewModelAdapter?.collectors = viewModel.collectorsOrigin.filter {
                        it.name.lowercase().contains(query.lowercase())
                    }
                }
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorsRv
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