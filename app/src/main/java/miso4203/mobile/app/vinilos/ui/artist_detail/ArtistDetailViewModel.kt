package miso4203.mobile.app.vinilos.ui.artist_detail

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
import miso4203.mobile.app.vinilos.models.ArtistDetail
import miso4203.mobile.app.vinilos.repositories.ArtistDetailRepository

class ArtistDetailViewModel(application: Application, private val artistId: Int) :
    AndroidViewModel(application) {

    private val _artistDetail = MutableLiveData<ArtistDetail>()
    private val artistDetailRepository = ArtistDetailRepository(application)

    val artistDetail: LiveData<ArtistDetail>
        get() = _artistDetail

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
                    _artistDetail.postValue(artistDetailRepository.refreshData(artistId))
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

    class Factory(private val application: Application, private val artistId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ArtistDetailViewModel(application, artistId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}