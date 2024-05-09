package miso4203.mobile.app.vinilos.ui.album_create

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

class AlbumCreateViewModel(application: Application) : AndroidViewModel(application) {
    private val _album = MutableLiveData<Album>()
    private val _albumRepository = AlbumRepository(
        application,
        VinylRoomDatabase.getDatabase(application.applicationContext).albumsDao()
    )

    var album: LiveData<Album>
        get() = _album

    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        album = MutableLiveData()
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun addNewAlbum(newAlbum: Album): Boolean {
        return try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    _albumRepository.addAlbum(newAlbum)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
            true
        } catch (e:Exception){
            _eventNetworkError.value = true
            false
        }

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumCreateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumCreateViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}