package com.phincon.qris.screen.payment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phincon.qris.utils.PreferencesManager
import com.phincon.qris.R
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.utils.formatToRupiah
import com.phincon.qris.utils.separateStringByDot
import java.time.LocalDateTime



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    qrResult: String,
    navController: NavHostController,
    paymentViewModel: PaymentViewModel,
    homeViewModel: HomeViewModel
) {

    var user by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf(0) }
    var bankName by remember { mutableStateOf("") }
    var idTransaction by remember { mutableStateOf("") }
    var merchantName by remember { mutableStateOf("") }
    var nominalTransaction by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    val state by paymentViewModel.state.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(context) {
        val preferencesManager = PreferencesManager(context)
        user = preferencesManager.getName("name", "")
        balance = preferencesManager.getBalance("balance",0)
    }


    state.let { state ->
        when (state) {
            is DatabaseStateEvent.Loading -> {
                loading = true
            }

            is DatabaseStateEvent.Success -> {
                val route = "status/${qrResult}"
                navController.navigate(route) {
                    popUpTo("status/${qrResult}") {
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
    if (qrResult != null) {
        val separatedWords = separateStringByDot(qrResult)
        bankName = separatedWords.getOrNull(0) ?: ""
        idTransaction = separatedWords.getOrNull(1) ?: ""
        merchantName = separatedWords.getOrNull(2) ?: ""
        nominalTransaction = separatedWords.getOrNull(3)?.toInt() ?: 0
        Log.d("Result Detail", qrResult)
    }
    Scaffold(topBar = {
        Column {
            CenterAlignedTopAppBar(title = { Text("Status") })
            Divider()
        }
    }, bottomBar = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider()
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(40.dp)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Button(
                    onClick = {
                        loading = true
                        val currentDateTime = LocalDateTime.now()
                        val history = HistoryTransaction(
                            user = user,
                            date = currentDateTime.toString(),
                            bankName = bankName,
                            merchantName = merchantName,
                            idTransaction = idTransaction,
                            nominal = nominalTransaction,
                            uid = null
                        )
                        paymentViewModel.insertHistory(history)
                        paymentViewModel.substractBalance(user,nominalTransaction)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Bayar")
                }
            }

        }

    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                    )
                    .align(Alignment.CenterHorizontally)
                    .border(
                        1.dp,
                        SolidColor(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,

                ) {
                Text(
                    text = "Info Pembayaran",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_semibold_600)
                    )
                )
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "Sukses Icon"
                )
                Text(
                    text = "Detail Transaksi",
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_medium_500)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bank Sumber",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = bankName, modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ID Transaksi",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = idTransaction, modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nama Merchant",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = merchantName, modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nominal Transaksi",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = formatToRupiah(nominalTransaction), modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }

                Text(
                    text = "Informasi Saldo",
                    modifier = Modifier.padding(start = 8.dp, top = 10.dp, bottom = 8.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_medium_500)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Saldo Anda",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = formatToRupiah(balance), modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }

            }


        }

    }
}

@Composable
@Preview
fun DetailScreenPreview() {
//    DetailScreen("")
}