package com.phincon.qris.screen.payment

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.utils.formatToRupiah
import com.phincon.qris.utils.separateStringByDot


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusPaymentScreen(
    qrResult: String,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    paymentViewModel: PaymentViewModel
) {
    var user by remember { mutableStateOf("") }
    var balanceAccount by remember { mutableStateOf(0) }
    var bankName by remember { mutableStateOf("") }
    var idTransaction by remember { mutableStateOf("") }
    var merchantName by remember { mutableStateOf("") }
    var nominalTransaction by remember { mutableStateOf(0) }
    val context = LocalContext.current
    LaunchedEffect(context) {
        val preferencesManager = PreferencesManager(context)
        user = preferencesManager.getName("name", "")
    }
    val userData by homeViewModel.getUserData(user).collectAsState(initial = null)
    userData.let {
        balanceAccount = it?.balance ?: 0
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
        Column {
            Divider()
            Button(
                onClick = {
                    paymentViewModel.resetState()
                    val route = "main/${user}"
                    navController.navigate(route) {
                        popUpTo("main/${user}") {
                            inclusive = true
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Kembali")
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
                    text = "Pembayaran Berhasil",
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
                    painter = painterResource(id = R.drawable.succes_icon),
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
                        text = bankName,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
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
                        text = idTransaction,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
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
                        text = merchantName,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
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
                        text = formatToRupiah(nominalTransaction),
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
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
                        text = "Saldo Awal",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = formatToRupiah(balanceAccount + nominalTransaction),
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sisa Saldo",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = formatToRupiah(balanceAccount),
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp,
                            end = 8.dp
                        ),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
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
fun StatusPaymentScreenPreview() {
//    StatusPaymentScreen("")
}