package com.phincon.qris.model


import com.google.gson.annotations.SerializedName

data class PromoResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("meta")
    val meta: Meta
) {
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("attributes")
        val attributes: Attributes
    ) {
        data class Attributes(
            @SerializedName("title")
            val title: String,
            @SerializedName("count")
            val count: Int,
            @SerializedName("alt")
            val alt: Int,
            @SerializedName("desc")
            val desc: String,
            @SerializedName("desc_promo")
            val descPromo: String,
            @SerializedName("latitude")
            val latitude: String,
            @SerializedName("longitude")
            val longitude: String,
            @SerializedName("lokasi")
            val lokasi: String,
            @SerializedName("nama")
            val nama: String,
            @SerializedName("name_promo")
            val namePromo: String,
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("updatedAt")
            val updatedAt: String
        )
    }

    data class Meta(
        @SerializedName("pagination")
        val pagination: Pagination
    ) {
        data class Pagination(
            @SerializedName("page")
            val page: Int,
            @SerializedName("pageSize")
            val pageSize: Int,
            @SerializedName("pageCount")
            val pageCount: Int,
            @SerializedName("total")
            val total: Int
        )
    }
}