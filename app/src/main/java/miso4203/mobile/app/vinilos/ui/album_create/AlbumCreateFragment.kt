package miso4203.mobile.app.vinilos.ui.album_create

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
import miso4203.mobile.app.vinilos.R
import miso4203.mobile.app.vinilos.databinding.FragmentAlbumCreateBinding
import miso4203.mobile.app.vinilos.models.Album
import java.util.Calendar

class AlbumCreateFragment: Fragment() {

    private var _binding: FragmentAlbumCreateBinding? = null
    private lateinit var viewModel: AlbumCreateViewModel
    private val binding get() = _binding!!
    private lateinit var dateEdt: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumCreateBinding.inflate(inflater, container, false)


        dateEdt = binding.dateAlbumDatepicker
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

        binding.btnCreateAlbum.setOnClickListener {
            val name = binding.nameAlbum.text?.toString()?:""
            val description = binding.descripcionAlbumTextField.text.toString()
            val cover = binding.coverAlbum.text.toString()
            val releaseDate = binding.dateAlbumDatepicker.text.toString()
            val genre = binding.genreAlbumTextField.selectedItem.toString()
            val recordLabel = binding.recordLabelAlbumTextField.text.toString()
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
                    showMessage("El álbum se guardo de forma satisfactoria.")
                    navigateToCollector()
                } else {
                    showMessage("Ocurrió un error al guardar el álbum.")
                }
            } else {
                showMessage("Todos los campos deben ser diligenciados. Intenta de nuevo!.")
            }

        }
    }

    private fun formIsValid(array: ArrayList<String>): Boolean {
        for (elem in array) {
            if (TextUtils.isEmpty(elem) || elem.length < 4) {
                return false
            }
        }
        return true
    }

   private fun navigateToCollector() {
       binding.root.findNavController().navigate(
           AlbumCreateFragmentDirections.actionAlbumCreateFragmentToCollectorFragment()
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