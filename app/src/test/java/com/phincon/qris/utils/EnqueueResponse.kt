package com.phincon.qris.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.InputStreamReader

object EnqueueResponse {

    fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)

        val source = InputStreamReader(inputStream)
        source?.let {
            enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setBody(
                        source.readText()
                    )
            )
        }
    }
}