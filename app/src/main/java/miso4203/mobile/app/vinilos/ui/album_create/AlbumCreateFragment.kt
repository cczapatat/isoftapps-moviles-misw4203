package miso4203.mobile.app.vinilos.ui.album_create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.FragmentAlbumCreateBinding
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.ui.track_add.TrackAddFragmentArgs
import java.text.SimpleDateFormat
import java.util.Calendar

class AlbumCreateFragment: Fragment() {

    private var _binding: FragmentAlbumCreateBinding? = null
    private lateinit var viewModel: AlbumCreateViewModel
    private val binding get() = _binding!!
    private lateinit var dateEdt: EditText

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumCreateBinding.inflate(inflater, container, false)

        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MMM-yyyy")
        val formattedDate: String = df.format(c.time)
        dateEdt = binding.dateAlbumDatepicker
        dateEdt.setText(formattedDate)
        dateEdt.showSoftInputOnFocus = false
        dateEdt.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                binding.root.context,
                { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

       binding.btnCancelAlbum.setOnClickListener {
           navigateToCollector()
        }

        binding.btnSaveAlbum.setOnClickListener {
            val name = binding.nameAlbum.text?.toString()?:""
            val description = binding.descripcionAlbumTextField.text.toString()
            val cover = binding.coverAlbum.text.toString()
            val releaseDateOld = binding.dateAlbumDatepicker.text.toString()
            val arr = releaseDateOld.split("-")
            val releaseDate = "${arr[1]}-${arr[0]}-${arr[2]}"
            val genre = binding.genreAlbumTextField.selectedItem.toString()
            val recordLabel = binding.recordLabelAlbumTextField.selectedItem.toString()
            val argsArray: ArrayList<String> = arrayListOf(name, description, cover, releaseDate, genre, recordLabel)
            if (this.formIsValid(argsArray)) {
                val album = Album(
                    name = name,
                    description = description,
                    cover = cover,
                    genre = genre,
                    releaseDate = releaseDate,
                    recordLabel = recordLabel
                )
                if (viewModel.addNewAlbum(album)) {
                    showMessage("The album was saved successfully")
                    navigateToCollector()
                } else {
                    showMessage("There was happened an error trying to save the album")
                }
            } else {
                showMessage("All of fields must be filled. Try again.")
            }

        }
    }

    private fun formIsValid(array: ArrayList<String>): Boolean {
        for (elem in array) {
            if (TextUtils.isEmpty(elem) || elem.length < 3) {
                return false
            }
        }
        return true
    }

   private fun navigateToCollector() {
       val args: AlbumCreateFragmentArgs by navArgs()

       binding.root.findNavController().navigate(
           AlbumCreateFragmentDirections.actionAlbumCreateFragmentToCollectorFragment(args.collectorId)
       )
    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        binding.btnBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }

        activity.actionBar?.title = getString(R.string.album_title)
        viewModel = ViewModelProvider(
            this,
            AlbumCreateViewModel.Factory(activity.application)
        )[AlbumCreateViewModel::class.java]
        viewModel.album.observe(viewLifecycleOwner) {
            it.apply {

            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }
}