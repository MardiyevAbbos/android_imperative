package com.example.android_imperative.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.model.TVShowDetails
import com.example.android_imperative.model.TVShowPopular
import com.example.android_imperative.repository.TVShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: TVShowRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val tvShowsFromApi = MutableLiveData<ArrayList<TVShow>>()
    val tvShowsFromDB = MutableLiveData<ArrayList<TVShow>>()

    val tvShowPopular = MutableLiveData<TVShowPopular>()
    val tvShowDetails = MutableLiveData<TVShowDetails>()

    /**
     * Retrofit Related
     */
    fun apiTvShowPopular(page: Int){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.apiTVShowPopular(page)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val resp = response.body()
                    tvShowPopular.postValue(resp!!)

                    tvShowsFromApi.postValue(resp!!.tv_shows)
                    isLoading.value = false
                }else{
                    onError("Error ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String){
        errorMessage.value = message
        isLoading.value = false
    }


    /**
     * Room Related
     */
    fun insertTVShowToDB(tvShow: TVShow){
        viewModelScope.launch {
            repository.insertTVShowToDB(tvShow)
        }
    }

    fun getTVShowsFromDB(){
        viewModelScope.launch {
            val response = repository.getTVShowsFromDB()
            if (response.isNotEmpty()){
                tvShowsFromDB.postValue(response as ArrayList<TVShow> /* = java.util.ArrayList<com.example.android_imperative.model.TVShow> */)
            }else{
                Log.d("TAG_DB", "getTVShowsFromDB: DB is Empty")
            }
        }
    }

    fun removeTVShowFromDB(id: Long){
        viewModelScope.launch {
            repository.removeTVShowFromDB(id)
        }
    }

}