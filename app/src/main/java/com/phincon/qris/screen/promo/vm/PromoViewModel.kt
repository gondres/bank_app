package com.phincon.qris.screen.promo.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phincon.qris.model.PromoResponse
import com.phincon.qris.service.repository.MainStateEvent
import com.phincon.qris.service.repository.PromoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoViewModel @Inject constructor(private val promoRepository: PromoRepository) : ViewModel() {
    private val _responseReviewProduct = MutableLiveData<PromoResponse>()
    private val _stateDetail = MutableLiveData<MainStateEvent<PromoResponse>>()

    val responseReviewProduct: LiveData<PromoResponse> = _responseReviewProduct
    val stateDetail: LiveData<MainStateEvent<PromoResponse>> = _stateDetail

    init {
        getPromos()
    }
    fun getPromos() {
        _stateDetail.value = MainStateEvent.Loading(null)
        viewModelScope.launch {
            val result = promoRepository.getPromos()
            when (result) {
                is MainStateEvent.Loading -> {
                    Log.d("Promo View Model","Loading")
                }

                is MainStateEvent.Success -> {
                Log.d("Promo View Model","Success")
                    _responseReviewProduct.postValue(result.data!!)
                }

                is MainStateEvent.Error -> {
                    Log.d("Promo View Model","Error")
                }

                is MainStateEvent.Exception -> {
                    Log.d("Promo View Model",result.errorMessage)
                }
            }
            _stateDetail.value = result
        }
    }
}