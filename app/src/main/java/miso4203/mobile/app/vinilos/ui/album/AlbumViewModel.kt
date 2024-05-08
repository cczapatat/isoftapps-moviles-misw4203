package miso4203.mobile.app.vinilos.ui.album

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
import miso4203.mobile.app.vinilos.repositories.AlbumRepository

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()
    private val _albumsOrigin = mutableListOf<Album>()
    private val albumRepository = AlbumRepository(
        application,
        VinylRoomDatabase.getDatabase(application.applicationContext).albumsDao()
    )

    val albums: LiveData<List<Album>>
        get() = _albums

    val albumsOrigin: List<Album>
        get() = _albumsOrigin

    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    val items = albumRepository.refreshData()
                    _albums.postValue(items)
                    _albumsOrigin.addAll(items)
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
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}