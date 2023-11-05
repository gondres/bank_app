package com.phincon.qris.screen.login.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.User
import com.phincon.qris.database.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _loginState = MutableLiveData<DatabaseStateEvent>()
    val loginState: LiveData<DatabaseStateEvent> = _loginState

    fun insertUser(user: User) {
        _loginState.value = DatabaseStateEvent.Loading
        viewModelScope.launch {
            val result = userRepository.insertUser(user)
            _loginState.value = result
        }
    }
}