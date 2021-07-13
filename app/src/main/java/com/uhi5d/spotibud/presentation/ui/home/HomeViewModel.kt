package com.uhi5d.spotibud.presentation.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.uhi5d.spotibud.domain.model.MyArtists
import com.uhi5d.spotibud.domain.model.Recommendations
import com.uhi5d.spotibud.domain.model.getArtistIds
import com.uhi5d.spotibud.domain.model.mytracks.MyTracks
import com.uhi5d.spotibud.domain.model.mytracks.getTrackIds
import com.uhi5d.spotibud.domain.model.recenttracks.RecentTracks
import com.uhi5d.spotibud.domain.usecase.UseCase
import com.uhi5d.spotibud.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val useCase: UseCase,
    private val sharedPreferences: SharedPreferences
) :ViewModel(){

    private val _artists : MutableLiveData<DataState<MyArtists>> = MutableLiveData()
    val artists : LiveData<DataState<MyArtists>> get() = _artists

    private val _tracks : MutableLiveData<DataState<MyTracks>> = MutableLiveData()
    val tracks : LiveData<DataState<MyTracks>> get() = _tracks

    private val _recentTracks : MutableLiveData<DataState<RecentTracks>> = MutableLiveData()
    val recentTracks : LiveData<DataState<RecentTracks>> get() = _recentTracks

    private val mediatorLiveData : MediatorLiveData<Pair<DataState<MyArtists>, DataState<MyTracks>>> =
        object : MediatorLiveData<Pair<DataState<MyArtists>, DataState<MyTracks>>>(){
            var myArtists: DataState<MyArtists>? = null
            var myTracks: DataState<MyTracks>? = null
            init {
                addSource(artists){ myArtists ->
                    this.myArtists = myArtists
                    myTracks?.let { value = myArtists to it }
                }
                addSource(tracks){ myTracks ->
                    this.myTracks = myTracks
                    myArtists?.let { value = it to myTracks }
                }
            }
        }

    private val _recommendations : MutableLiveData<DataState<Recommendations>> = MutableLiveData()
    val recommendations : LiveData<DataState<Recommendations>> get() = _recommendations



    private fun getToken() = sharedPreferences.getString("token", null)

    fun getRecommendations() {
        getMyTopArtists()
        getMyTopTracks()

        mediatorLiveData.observeForever { pair ->
            if (pair.first is DataState.Success && pair.second is DataState.Success) {
                viewModelScope.launch {
                    useCase.getRecommendations(
                        getToken()!!,
                        (pair.first as DataState.Success<MyArtists>).data.getArtistIds(),
                        (pair.second as DataState.Success<MyTracks>).data.getTrackIds()
                    )
                        .collect {
                            _recommendations.value = it
                        }
                }
            }
        }}

    fun getRecentTracks() = viewModelScope.launch {
        val token = getToken()
        if (token != null){
            useCase.getMyRecentPlayed(token).collect { state ->
                when(state){
                    is DataState.Success -> {
                        _recentTracks.value = state
                    }
                    DataState.Empty -> _recentTracks.value = state
                    is DataState.Fail -> {
                        _recentTracks.value = state
                    }
                    DataState.Loading -> _recentTracks.value = state
                }
            }
        }

    }


    private fun getMyTopArtists() = viewModelScope.launch {
        useCase.getMyTopArtists(getToken()!!, "short-term", 2)
            .collect { state ->
                _artists.value = state
            }
    }

    private fun getMyTopTracks() = viewModelScope.launch {
        useCase.getMyTopTracks(getToken()!!, "short-term", 2)
            .collect { state ->
                _tracks.value = state
            }
    }
}