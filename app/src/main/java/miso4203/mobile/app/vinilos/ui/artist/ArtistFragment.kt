package miso4203.mobile.app.vinilos.ui.artist

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
import miso4203.mobile.app.vinilos.databinding.FragmentArtistBinding
import miso4203.mobile.app.vinilos.ui.adapters.ArtistsAdapter
import miso4203.mobile.app.vinilos.ui.album.AlbumFragment

class ArtistFragment : Fragment() {

    private var _binding: FragmentArtistBinding? = null

    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistViewModel
    private var viewModelAdapter: ArtistsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        viewModelAdapter = ArtistsAdapter()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val searchView = activity.findViewById<SearchView>(R.id.searchView)

        viewModel = ViewModelProvider(
            this, ArtistViewModel.Factory(activity.application)
        )[ArtistViewModel::class.java]

        viewModel.artists.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.artists = this
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
                    viewModelAdapter?.artists = viewModel.artistsOrigin
                    return
                } else if (query.length >= AlbumFragment.ACCEPTABLE_QUERY_STRING_LENGHT) {
                    viewModelAdapter?.artists = viewModel.artistsOrigin.filter {
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
        recyclerView = binding.artistRv
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