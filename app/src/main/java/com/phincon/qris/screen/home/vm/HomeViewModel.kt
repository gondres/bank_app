package com.phincon.qris.screen.home.vm

import androidx.lifecycle.ViewModel
import com.phincon.qris.database.repository.HistoryRepository
import com.phincon.qris.database.repository.UserRepository
import com.phincon.qris.service.repository.PromoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository, private val historyRepository: HistoryRepository) : ViewModel() {

    fun getUserData(name: String) = userRepository.getUserData(name)

    fun getHistoryList(name: String) = historyRepository.getHistoryList(name)
}