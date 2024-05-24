package miso4203.mobile.app.vinilos.ui.track_add

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import miso4203.mobile.app.vinilos.databinding.FragmentTrackAddBinding
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Track
import miso4203.mobile.app.vinilos.ui.album_create.AlbumCreateFragmentDirections
import miso4203.mobile.app.vinilos.ui.collector.CollectorFragmentDirections
import miso4203.mobile.app.vinilos.ui.collector_detail.CollectorDetailFragmentArgs

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
    private fun navigateToCollectorDetail() {
        val args: TrackAddFragmentArgs by navArgs()
        val collectorRealID = args.collectorId
        if(collectorRealID == -1){
            binding.root.findNavController().navigate(
                TrackAddFragmentDirections.actionTrackAddFragmentToAlbumDetailFragment(args.albumId)
            )
        } else{
            binding.root.findNavController().navigate(
                TrackAddFragmentDirections.actionTrackAddFragmentToCollectorDetailFragment(args.collectorId)
            )
        }

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

        val args: TrackAddFragmentArgs by navArgs()
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
                val pos = if (args.albumId > 0) {
                    adp.getPosition(itList.find { args.albumId == it.id })
                } else 0
                spinner.adapter = adp
                spinner.setSelection(pos)
            }

            //Set to true if there are errors on the screen
            var errorsOnScreen = false;

            binding.txtTrackAddDuration.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable?) {

                    if(s.toString().length < 5)
                        return

                    errorsOnScreen = !s.toString().matches(Regex("^([0-5]?[0-9]):([0-5][0-9])\$"))
                    binding.txtTrackAddDuration.error =
                        if (errorsOnScreen) "Duration must be in format mm:ss" else null
                }
            })


            binding.txtTrackAddName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val trackName = binding.txtTrackAddName.text.toString()
                    errorsOnScreen = TextUtils.isEmpty(trackName) || trackName.length < 3
                    binding.txtTrackAddName.error =
                        if (errorsOnScreen) "The track name must have at least 3 characters" else null
                }
            }

            binding.btnSaveTrack.setOnClickListener {
                val album = binding.spinnerAlbumAddTrack.selectedItem as Album
                val trackName = binding.txtTrackAddName.text.toString()
                val duration = binding.txtTrackAddDuration.text.toString()

                val isValidForm = this.formIsValid(
                    arrayListOf(
                        album.id.toString(),
                        trackName,
                        duration
                    )
                ) && !errorsOnScreen
                val isTrackAdded =
                    isValidForm && viewModel.addNewTrack(album.id, Track(0, trackName, duration))

                val message = when {
                    isTrackAdded -> "The track was saved successfully"
                    isValidForm -> "There was happened an error trying to save the track"
                    else -> "All of fields must be filled, or some data is invalid"
                }

                showMessage(message)
                navigateToCollectorDetail()
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