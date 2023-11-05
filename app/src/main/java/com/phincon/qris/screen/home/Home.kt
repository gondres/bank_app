package com.phincon.qris.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phincon.qris.R
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.utils.PreferencesManager
import com.phincon.qris.utils.formatToRupiah


@Composable
@Preview
fun HomeScreenPreview() {
//    HomeScreen()
}

@Composable
@Preview(showBackground = true)
fun HomeScreen(args: String?, homeViewModel: HomeViewModel) {

    var name by remember { mutableStateOf("") }
    var balanceAccount by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val userData by homeViewModel.getUserData(args!!).collectAsState(initial = null)
    userData.let {
        balanceAccount = it?.balance ?: 0
        preferencesManager.saveBalance("balance", it?.balance ?: 0)
    }
    val historyList by homeViewModel.getHistoryList(args!!).collectAsState(initial = null)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    SolidColor(MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxWidth()
        ) {
            Text(
                text = "Saldo Anda",
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(R.font.poppins_regular_400)
                )
            )
            Text(
                text = formatToRupiah(balanceAccount),
                modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp),
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(R.font.poppins_semibold_600)
                )
            )
        }

        Text(
            text = "Riwayat Transaksi",
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            fontSize = 14.sp,
            fontFamily = FontFamily(
                Font(R.font.poppins_regular_400)
            )
        )
        if (historyList?.isEmpty() == true) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.ic_state),
                    contentDescription = ""
                )
                Text(
                    text = "Kosong",
                    modifier = Modifier.padding( top = 8.dp),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_medium_500)
                    )
                )
            }

        } else {
            Column {
                historyList?.let { HistoryLazyColumn(it) }
            }
        }


    }
}


@Composable
fun HistoryLazy(items: List<HistoryTransaction>) {
    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        items(items) { item ->
            ListHistory(
                item
            )
        }
    }
}

@Composable
fun ListHistory(item: HistoryTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_money),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = item.merchantName ?: "-",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatToRupiah(item.nominal ?: 0),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun HistoryLazyColumn(items: List<HistoryTransaction>) {
    HistoryLazy(items = items)
}