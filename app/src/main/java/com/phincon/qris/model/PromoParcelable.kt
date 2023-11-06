package com.phincon.qris.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PromoParcelable(
    val title: String,
    val desc: String,
    val location: String,
    val createdAt: String
) : Parcelable