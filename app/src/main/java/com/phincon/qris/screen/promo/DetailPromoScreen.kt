package com.phincon.qris.screen.promo

import android.os.Build
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phincon.qris.R
import com.phincon.qris.model.PromoParcelable
import com.phincon.qris.utils.convertToDDMMYYYY


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun DetailPromoScreen(
    navController: NavHostController,
    promo: PromoParcelable
) {
    Scaffold(topBar = {
        Column {
            CenterAlignedTopAppBar(title = { Text("Detail Promo") })
            Divider()
        }
    }, bottomBar = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider()
            Button(
                onClick = {
                    navController.popBackStack()

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
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "Info Promo",
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
                    painter = painterResource(id = R.drawable.ic_promo),
                    contentDescription = "Sukses Icon"
                )
                Text(
                    text = if(promo.title == "<null>") "Tidak ada judul" else promo.title,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_medium_500)
                    )
                )
                Text(
                    text = if(promo.desc == "<null>") "Tidak ada deskripsi" else promo.desc,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular_400)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Lokasi",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = promo.location, modifier = Modifier.padding(
                            start = 8.dp, top = 2.dp, bottom = 8.dp, end = 8.dp
                        ), fontSize = 14.sp, fontFamily = FontFamily(
                            Font(R.font.poppins_semibold_600)
                        )
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tanggal Promo",
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.poppins_regular_400)
                        )
                    )
                    Text(
                        text = convertToDDMMYYYY(promo.createdAt), modifier = Modifier.padding(
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
fun DetailPromoScreenPreview() {
//    DetailPromoScreen("")
}