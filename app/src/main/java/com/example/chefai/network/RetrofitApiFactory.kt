package com.example.chefai.network

import com.example.chefai.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiFactory {

    companion object {

        fun retroInstance(): Retrofit {
            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()

            client.addInterceptor(logging)

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
    }

}