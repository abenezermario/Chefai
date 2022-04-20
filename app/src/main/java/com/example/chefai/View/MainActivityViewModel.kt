package com.example.chefai

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    lateinit var createPostLiveData: MutableLiveData<PostResponseData?>
    init {
        createPostLiveData = MutableLiveData()
    }

    fun getPostLiveObserver(): MutableLiveData<PostResponseData?> {
        return createPostLiveData
    }


    fun createPostData(postdata: PostData) {
        val retroService = RetrofitInstance.retroInstance().create(ApiService::class.java)

        val call = retroService.postData(postdata)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    createPostLiveData.postValue(response.body())
                    var text = response.body()!!.choices[0].text
                    Log.d("response", "OK")
                    text.lines().forEach { println(it)}


                } else {
                    createPostLiveData.postValue(null)
                    Log.d("failed", response.errorBody().toString())
                }

            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.d("failed", t.message.toString())
                createPostLiveData.postValue(null)
            }

        })

    }
}