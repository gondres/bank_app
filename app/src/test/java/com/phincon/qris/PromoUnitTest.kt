package com.phincon.qris

import com.phincon.qris.model.PromoResponse
import com.phincon.qris.service.ApiService
import com.phincon.qris.utils.EnqueueResponse.enqueueResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals


@RunWith(JUnit4::class)
class PromoUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    val gsonConverterFactory = GsonConverterFactory.create()

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val okHttpClient = OkHttpClient()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiService::class.java)

    }

    @After
    fun shutdownMock(){
        mockWebServer.shutdown()
    }

    @Test
    fun `test API get promo`(){
        mockWebServer.enqueueResponse("promo_response.json", 200)
        val expected = PromoResponse(
            data = listOf(
                PromoResponse.Data(
                    id = 1,
                    attributes = PromoResponse.Data.Attributes(
                        title = "<null>",
                        count = 14,
                        alt = 17,
                        desc = "Potongan langsung (diskon) Rp 150.000,- untuk minimal transaksi Rp 1.000.000, kuota 15 transaksi pertama per hari.\\n- Berlaku tiap Kamis dan Jumat.\\n- Berlaku untuk pembelian Tiket Sriwijaya Air dan NAM Air di Website dan Mobile Apps Sriwijaya Air\\n- Potongan harga langsung diperoleh ketika nomor Kartu BNI dimasukkan (tanpa kode promo)\\n- Syarat dan ketentuan berlaku\\nInfo lebih lanjut hubungi BNI Call 1500046",
                        descPromo = "<null>",
                        latitude = "-6.203606",
                        longitude = "106.803022",
                        lokasi = "Pejompongan",
                        nama = "BNI Credit Card",
                        namePromo = "<null>",
                        createdAt = "2023-10-31T20:02:23.175Z",
                        updatedAt = "2023-10-31T20:02:23.175Z"
                    )
                ),
                PromoResponse.Data(
                    id = 3,
                    attributes =  PromoResponse.Data.Attributes(
                        title = " the hat",
                        count = 28,
                        alt = 19,
                        desc = "It was a simple tip of the hat. Grace didn't think that anyone else besides her had even noticed it. It wasn't anything that the average person would notice, let alone remember at the end of the day. That's why it seemed so unbelievable that this little gesture would ultimately change the course of the world.",
                        descPromo = " the hat",
                        latitude = "-82.031063965501",
                        longitude = "-124.612081930480",
                        lokasi = "Somewhere",
                        nama = "hat",
                        namePromo = "hat",
                        createdAt = "2023-11-01T09:42:38.452Z",
                        updatedAt = "2023-11-06T08:36:18.464Z"
                    )
                ),

            ),
            meta = PromoResponse.Meta(
                pagination = PromoResponse.Meta.Pagination(
                    page = 1,
                    pageSize = 25,
                    pageCount = 1,
                    total = 4
                )
            )
        )

        runBlocking{
            val actual = apiService.getPromos()
            assertEquals(expected,actual.body())
        }
    }
}