package com.example.chefai.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(val email: String? = null, val username: String? = null) : Parcelable
