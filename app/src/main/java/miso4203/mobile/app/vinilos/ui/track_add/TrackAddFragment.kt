package miso4203.mobile.app.vinilos.ui.track_add

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import miso4203.mobile.app.vinilos.databinding.FragmentTrackAddBinding
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Track

class TrackAddFragment : Fragment() {

    private var _binding: FragmentTrackAddBinding? = null
    private lateinit var viewModel: TrackAddViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        binding.btnBackTrackAdd.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCancelTrack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }

        viewModel = ViewModelProvider(
            this, TrackAddViewModel.Factory(activity.application)
        )[TrackAddViewModel::class.java]

        viewModel.albums.observe(viewLifecycleOwner) { itList ->
            itList.apply {
                val spinner = binding.spinnerAlbumAddTrack
                val adp = ArrayAdapter(
                    binding.root.context, android.R.layout.simple_spinner_item, itList
                )
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adp
            }

            binding.btnSaveTrack.setOnClickListener {
                val album = binding.spinnerAlbumAddTrack.selectedItem as Album
                val trackName = binding.txtTrackAddName.text.toString()
                val duration = binding.txtTrackAddDuration.text.toString()

                if (this.formIsValid(arrayListOf(album.id.toString(), trackName, duration))) {
                    if (viewModel.addNewTrack(album.id, Track(0, trackName, duration))) {
                        showMessage("The track was saved successfully")
                        activity.onBackPressedDispatcher.onBackPressed()
                    } else {
                        showMessage("There was happened an error trying to save the track")
                    }
                } else {
                    showMessage("All of fields must be filled. Try again.")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formIsValid(array: ArrayList<String>): Boolean {
        for (elem in array) {
            if (TextUtils.isEmpty(elem) || elem.length < 3) {
                return false
            }
        }
        return true
    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }
}