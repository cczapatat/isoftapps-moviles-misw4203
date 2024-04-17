package miso4203.mobile.app.vinilos.ui.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArtistViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is artist Fragment"
    }
    val text: LiveData<String> = _text
}