package com.phincon.qris.screen.promo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.phincon.qris.MainActivity
import com.phincon.qris.R
import com.phincon.qris.model.PromoParcelable
import com.phincon.qris.model.PromoResponse
import com.phincon.qris.navigation.RouteNav
import com.phincon.qris.screen.promo.vm.PromoViewModel
import com.phincon.qris.service.repository.MainStateEvent
import com.phincon.qris.utils.convertToDDMMYYYY
import com.valentinilk.shimmer.shimmer


@Composable
fun PromoScreen(navController : NavHostController,promoViewModel: PromoViewModel = viewModel()) {

    var isLoading by remember { mutableStateOf(true) }
    val state by promoViewModel.stateDetail.observeAsState()
    val promoResponse by promoViewModel.responseReviewProduct.observeAsState()


    state.let { state ->
        when (state) {
            is MainStateEvent.Loading -> {
                isLoading = true
                Log.d("Promo Screen","Loading")
            }

            is MainStateEvent.Success -> {
                isLoading = false
                Log.d("Promo Screen","Success")
            }

            is MainStateEvent.Error -> {
            }

            is MainStateEvent.Exception -> {
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if(isLoading){
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(5){
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = Color.White, shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                SolidColor(Color.Gray),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .fillMaxWidth()
                            .shimmer()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp, top = 8.dp)
                                    .width(100.dp)
                                    .height(20.dp)
                                    .background(Color.Gray)
                                    .shimmer()
                            )
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp, top = 8.dp)
                                    .width(80.dp)
                                    .height(20.dp)
                                    .background(Color.Gray)
                                    .shimmer()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp)
                                .width(140.dp)
                                .height(20.dp)
                                .background(Color.Gray)
                                .shimmer()
                        )

                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                                .width(100.dp)
                                .height(20.dp)
                                .background(Color.Gray)
                                .shimmer()
                        )
                    }
                }
            }
        }else{
            if(promoResponse != null){
                PromoLazy(navController,items = promoResponse!!.data )
            }

        }

    }

}

@Composable
@Preview
fun PromoScreenPreview() {
//    PromoScreen()
}


@Composable
fun PromoLazy(navController: NavHostController,items: List<PromoResponse.Data>) {
    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        items(items) { item ->
            ListPromo(
                navController,
                item
            )
        }
    }
}

@Composable
fun ListPromo(navController: NavHostController,item: PromoResponse.Data) {
    val activity = LocalView.current.context as? MainActivity
    Column {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    SolidColor(MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxWidth()
                .clickable {
                    val promo = PromoParcelable(
                        title = item.attributes.title,
                        desc = item.attributes.desc,
                        location =  item.attributes.lokasi,
                        createdAt = item.attributes.createdAt
                    )

                   activity?.navigateToPromoDetail(promo)
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if(item.attributes.title == "<null>") "-" else item.attributes.title,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular_400)
                    )
                )
                Text(
                    text = convertToDDMMYYYY(item.attributes.createdAt),
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular_400)
                    )
                )
            }

            Text(
                text = if(item.attributes.descPromo == "<null>") "-" else item.attributes.descPromo,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(R.font.poppins_semibold_600)
                )
            )

            Text(
                text = "Lokasi: ${item.attributes.lokasi}",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily(
                    Font(R.font.poppins_regular_400)
                )
            )
        }
    }

}

