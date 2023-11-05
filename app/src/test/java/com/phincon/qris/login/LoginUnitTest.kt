package com.phincon.qris.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Dao
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.utils.MainDispatcherRule
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.dao.UserDao
import com.phincon.qris.database.entity.User
import com.phincon.qris.database.repository.UserRepository
import com.phincon.qris.screen.login.vm.LoginViewModel
import com.phincon.qris.utils.getOrAwaitValue
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.Executors
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class LoginUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    val user = User(
        uid = null,
        name = "test",
        balance = 0,
        createdDate = "12/12/12"
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
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `test login success`() = runTest {
        whenever(userRepository.insertUser(user)).thenReturn(DatabaseStateEvent.Success(""))
        loginViewModel.insertUser(user)
        Assert.assertEquals(DatabaseStateEvent.Loading, loginViewModel.loginState.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(DatabaseStateEvent.Success(""),loginViewModel.loginState.getOrAwaitValue())
    }

    @Test
    fun `store data user to database`() = runTest {
        whenever(userRepository.insertUser(user)).thenReturn(DatabaseStateEvent.Success(""))
        val result = userRepository.insertUser(user)
        assertEquals(DatabaseStateEvent.Success(""),result)
    }
}