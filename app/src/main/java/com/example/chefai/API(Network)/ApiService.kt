package com.example.chefai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {


  @Headers("Accept:application/json","Content-Type: application/json",
    "Authorization: Bearer API_KEY"
    )
  @POST(Constants.ENDPOINT)
  fun postData(@Body postData: PostData): Call<PostResponseData>


}