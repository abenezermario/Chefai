package com.example.chefai.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class RecipeData(val recipeTitle: String?=null, val recipeData: String?= null): Parcelable
