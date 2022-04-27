package com.example.chefai.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chefai.ChefApi
import com.example.chefai.PostData
import com.example.chefai.PostResponseData
import com.example.chefai.network.RetrofitApiFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityViewModel : ViewModel() {

    var _createPostLiveData = MutableLiveData<PostResponseData?>()
    val createPostLiveData get() = _createPostLiveData

    fun createPostData(postdata: PostData) {
        val retroService = RetrofitApiFactory.retroInstance().create(ChefApi::class.java)

        val call = retroService.postData(postdata)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    _createPostLiveData.value = response.body()
                    var text = response.body()!!.choices[0].text
                    Log.d("response", text)


                } else {
                    _createPostLiveData.value = null
                    Log.d("failed", response.errorBody().toString())
                }

            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.d("failed", t.message.toString())
                _createPostLiveData.value = null
            }

        })

    }
}