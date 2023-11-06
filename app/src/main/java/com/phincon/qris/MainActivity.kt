package com.phincon.qris

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.model.PromoParcelable
import com.phincon.qris.navigation.BottomNavGraph
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.screen.login.vm.LoginViewModel
import com.phincon.qris.screen.promo.vm.PromoViewModel
import com.phincon.qris.ui.theme.Qris_mobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by viewModels()
    private lateinit var navController: NavHostController
    private var textResult = mutableStateOf("")

    private val barcodeLauncer = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            textResult.value = result.contents
            Log.d("Result QR Code", textResult.value)
            val route = "detail/${textResult.value}"
            navController.navigate(route)
        }
    }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR Code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)

        barcodeLauncer.launch(options)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCamera()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user_bank_db"
        ).build()

        setContent {
            navController = rememberNavController()
            Qris_mobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomNavGraph(
                        navController = navController,
                        loginViewModel,
                        homeViewModel,
                        paymentViewModel
                    )
                }
            }
        }
    }

    fun checkCameraPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    fun navigateToChartDetail(label: String){
        val route = "detailChart/${label}"
        navController.navigate(route)
    }

    fun navigateToPromoDetail(promo: PromoParcelable){
        val route = "detailPromo"
        navController.currentBackStackEntry?.savedStateHandle?.set(
            key = "promo",
            value = promo
        )
        navController.navigate(route)
    }

}

