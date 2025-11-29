package com.example.myexpenses.viewmodel

import androidx.lifecycle.*
import com.example.myexpenses.data.RetrofitInstance
import kotlinx.coroutines.launch

class RateViewModel : ViewModel() {

    private val _usdToByn = MutableLiveData<Double?>()
    val usdToByn: LiveData<Double?> get() = _usdToByn

    fun loadRate() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRate(431)
                _usdToByn.value = response.Cur_OfficialRate / response.Cur_Scale
            } catch (e: Exception) {
                e.printStackTrace()
                _usdToByn.value = null
            }
        }
    }
}
