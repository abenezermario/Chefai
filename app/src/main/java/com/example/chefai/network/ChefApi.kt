package com.example.chefai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChefApi {

  @Headers("Accept:application/json","Content-Type: application/json",
    "Authorization: Bearer "+ BuildConfig.OPENAI_KEY
    )
  @POST(Constants.ENDPOINT)
  fun postData(@Body postData: PostData): Call<PostResponseData>

}