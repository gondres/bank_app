package com.phincon.qris.screen.login

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.phincon.qris.MainActivity
import com.phincon.qris.utils.PreferencesManager
import com.phincon.qris.R
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.User
import com.phincon.qris.navigation.RouteNav
import com.phincon.qris.screen.login.vm.LoginViewModel
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview(showBackground = true)
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val activity = LocalView.current.context as? MainActivity
    val keyboardController = LocalSoftwareKeyboardController.current
    var name by remember { mutableStateOf("") }
    val loginState by loginViewModel.loginState.observeAsState()
    var loading by remember { mutableStateOf(false) }
    loginState.let { state ->
        when (state) {
            is DatabaseStateEvent.Loading -> {

            }

            is DatabaseStateEvent.Success -> {
                LaunchedEffect(context) {
                    val preferencesManager = PreferencesManager(context)
                    preferencesManager.saveName("name", name)
                }
                val route = "main/${name}"

                navController.navigate(route) {
                    popUpTo(RouteNav.Main.route) {
                        inclusive = true
                    }
                }
            }

            is DatabaseStateEvent.Error -> {
                loading = false
            }

            else -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Masukkan Nama Anda",
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(R.font.poppins_semibold_600)
            )
        )
        TextField(
            modifier = Modifier.padding(bottom = 8.dp),
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text("Nama") }
        )
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(40.dp)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Button(onClick = {
                if(name.isNotEmpty()){
                    loading = true
                    val currentDateTime = LocalDateTime.now()
                    val user = User(
                        uid = null,
                        name = name,
                        balance = 1000000,
                        createdDate = currentDateTime.toString()
                    )
                    keyboardController?.hide()
                    loginViewModel.insertUser(user).toString()
                }else{
                    Toast.makeText(activity, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }

            }) {
                Text("Masuk")
            }
        }

    }
}

