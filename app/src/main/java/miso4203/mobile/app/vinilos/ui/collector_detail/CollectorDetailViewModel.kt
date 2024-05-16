package miso4203.mobile.app.vinilos.ui.collector_detail

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
import miso4203.mobile.app.vinilos.models.CollectorAlbum
import miso4203.mobile.app.vinilos.repositories.CollectorDetailRepository

class CollectorDetailViewModel(application: Application, private val collectorId: Int):
AndroidViewModel(application) {

    private val _collectorAlbums = MutableLiveData<List<CollectorAlbum>>()
    private val collectorDetailRepository = CollectorDetailRepository(
        application,
        VinylRoomDatabase.getDatabase(application.applicationContext).collectorAlbumsDao()
    )

    val collectorAlbums: LiveData<List<CollectorAlbum>>
        get() = _collectorAlbums

    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    fun getData () {
        refreshDataFromNetwork()
    }

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    val items = collectorDetailRepository.refreshData(collectorId)
                    _collectorAlbums.postValue(items)
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

    class Factory(private val application: Application, private val collectorId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return CollectorDetailViewModel(application, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}