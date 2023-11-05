package com.phincon.qris

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.utils.MainDispatcherRule
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.dao.UserDao
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.entity.User
import com.phincon.qris.database.repository.HistoryRepository
import com.phincon.qris.database.repository.UserRepository
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.utils.getOrAwaitValue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.Executors
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class PaymentUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var historyRepository: HistoryRepository
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    val user = User(
        uid = null,
        name = "test",
        balance = 0,
        createdDate = "12/12/12"
    )

    val name = "test"

    val historyTransaction = HistoryTransaction(
        uid = 0,
        user = "test",
        idTransaction = "test",
        nominal = 0,
        merchantName = "test",
        bankName = "test",
        date = "12/12/12"
    )
    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        userDao = database.userDao()
        userRepository = Mockito.mock()
        historyRepository = Mockito.mock()
        paymentViewModel = PaymentViewModel(historyRepository,userRepository)
        homeViewModel = HomeViewModel(userRepository,historyRepository)
    }


    @Test
    fun `test get data user from repository`() = runTest {

        whenever(userRepository.getUserData(name)).thenReturn(flowOf(user))
        homeViewModel.getUserData(name)
        val result = userRepository.getUserData(name).first()
        assertEquals(user,result)
    }

    @Test
    fun `test get data user from view model`() = runTest {
        whenever(userRepository.getUserData(name)).thenReturn(flowOf(user))
       val result = homeViewModel.getUserData(name).first()
       assertEquals(user, result)
    }

    fun separateStringByDot(input: String): List<String> {
        return input.split(".")
    }
    @Test
    fun `Split QR Code result`(){
        val qrCodeResult = separateStringByDot("BNI.ID12345678.MERCHANT MOCK TEST.50000")

        assertEquals("BNI",qrCodeResult.getOrNull(0) ?: "")
        assertEquals("ID12345678",qrCodeResult.getOrNull(1) ?: "")
        assertEquals("MERCHANT MOCK TEST",qrCodeResult.getOrNull(2) ?: "")
        assertEquals("50000",qrCodeResult.getOrNull(3) ?: "")
    }

    @Test
    fun `test insert payment transaction`() = runTest {
        whenever(historyRepository.insertHistory(historyTransaction)).thenReturn(DatabaseStateEvent.Success(""))
        paymentViewModel.insertHistory(historyTransaction)
        Assert.assertEquals(DatabaseStateEvent.Loading, paymentViewModel.state.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(DatabaseStateEvent.Success(""),paymentViewModel.state.getOrAwaitValue())
    }

    @Test
    fun `test get data history transaction`() = runTest {
        whenever(historyRepository.getHistoryList(name)).thenReturn(flowOf(listOf(historyTransaction)))
        val result = homeViewModel.getHistoryList(name).first()
        assertEquals(listOf(historyTransaction), result)
    }
}