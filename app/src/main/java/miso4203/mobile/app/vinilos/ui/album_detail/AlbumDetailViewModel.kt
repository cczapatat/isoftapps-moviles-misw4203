package miso4203.mobile.app.vinilos.ui.album_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.repositories.AlbumDetailRepository

class AlbumDetailViewModel(application: Application, private val albumId: Int) :
    AndroidViewModel(application) {

    private val _album = MutableLiveData<AlbumDetail>()
    private val albumDetailRepository = AlbumDetailRepository(application)

    val album: LiveData<AlbumDetail>
        get() = _album

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
        this.albumDetailRepository.refreshData(this.albumId, {
            _album.postValue(it)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        }, {
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(private val application: Application, private val albumId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return AlbumDetailViewModel(application, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}