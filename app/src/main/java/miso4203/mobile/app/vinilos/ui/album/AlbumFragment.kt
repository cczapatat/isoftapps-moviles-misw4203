package miso4203.mobile.app.vinilos.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.FragmentAlbumBinding
import miso4203.mobile.app.vinilos.ui.adapters.AlbumsAdapter

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null

    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private var viewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        viewModelAdapter = AlbumsAdapter()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val searchView = activity.findViewById<SearchView>(R.id.searchView)

        viewModel = ViewModelProvider(
            this, AlbumViewModel.Factory(activity.application)
        )[AlbumViewModel::class.java]

        viewModel.albums.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.albums = this
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
                    viewModelAdapter?.albums = viewModel.albumsOrigin
                    return
                }

                val itemsFiltered = viewModel.albumsOrigin.filter {
                    it.name.lowercase().contains(query.lowercase())
                }
                viewModelAdapter?.albums = itemsFiltered
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelAdapter = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}