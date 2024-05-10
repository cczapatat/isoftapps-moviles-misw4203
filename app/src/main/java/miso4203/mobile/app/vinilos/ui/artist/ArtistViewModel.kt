package miso4203.mobile.app.vinilos.ui.artist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miso4203.mobile.app.vinilos.database.VinylRoomDatabase
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.repositories.ArtistRepository

class ArtistViewModel(application: Application) : ViewModel() {

    private val _artists = MutableLiveData<List<Artist>>()
    private val _artistsOrigin = mutableListOf<Artist>()
    private val artistRepository = ArtistRepository(
        application,
        VinylRoomDatabase.getDatabase(application.applicationContext).artistsDao()
    )

    val artists: LiveData<List<Artist>>
        get() = _artists

    val artistsOrigin: List<Artist>
        get() = _artistsOrigin

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
                    val items = artistRepository.refreshData()
                    _artists.postValue(items)
                    _artistsOrigin.addAll(items)
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
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ArtistViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct the view model")
        }
    }
}