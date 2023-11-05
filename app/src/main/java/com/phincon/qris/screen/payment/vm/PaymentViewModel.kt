package com.phincon.qris.screen.payment.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.repository.HistoryRepository
import com.phincon.qris.database.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val historyRepository: HistoryRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _state = MutableLiveData<DatabaseStateEvent?>()
    val state: LiveData<DatabaseStateEvent?> = _state

    fun insertHistory(history: HistoryTransaction) {
        _state.value = DatabaseStateEvent.Loading
        viewModelScope.launch {
            val result = historyRepository.insertHistory(history)
            _state.value = result
        }
    }

    fun substractBalance(name: String, amount: Int) {
        _state.value = DatabaseStateEvent.Loading
        viewModelScope.launch {
            val result = userRepository.substractBalance(name, amount)
            _state.value = result
        }
    }

    fun resetState(){
        _state.value = null
    }
}