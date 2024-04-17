package miso4203.mobile.app.vinilos.ui.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CollectorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is collector Fragment"
    }
    val text: LiveData<String> = _text
}