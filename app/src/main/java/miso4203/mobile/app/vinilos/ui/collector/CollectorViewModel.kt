package miso4203.mobile.app.vinilos.ui.collector

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miso4203.mobile.app.vinilos.database.VinylRoomDatabase
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Collector
import miso4203.mobile.app.vinilos.repositories.AlbumRepository
import miso4203.mobile.app.vinilos.repositories.CollectorRepository
import miso4203.mobile.app.vinilos.ui.album.AlbumViewModel

class CollectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()
    private val _collectorsOrigin = mutableListOf<Collector>()
    private val collectorRepository = CollectorRepository(
        application,
        VinylRoomDatabase.getDatabase(application.applicationContext).collectorsDao()
    )

    val collectors: LiveData<List<Collector>>
        get() = _collectors

    val collectorsOrigin: List<Collector>
        get() = _collectorsOrigin

    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    fun getData () {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    val items = collectorRepository.refreshData()
                    _collectors.postValue(items)
                    _collectorsOrigin.clear()
                    _collectorsOrigin.addAll(items)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        } catch (_: Exception) {
            _eventNetworkError.postValue(true)
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return CollectorViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct the view model")
        }
    }
}