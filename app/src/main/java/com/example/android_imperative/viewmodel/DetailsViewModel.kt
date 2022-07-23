package com.example.android_imperative.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.model.TVShowDetails
import com.example.android_imperative.repository.TVShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: TVShowRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val tvShowDetails = MutableLiveData<TVShowDetails>()

    fun apiTVShowDetails(show_id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.apiTVShowDetails(show_id)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    tvShowDetails.postValue(response.body())
                    isLoading.value = false
                }else{
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String){
        errorMessage.value = message
        isLoading.value = false
    }

}