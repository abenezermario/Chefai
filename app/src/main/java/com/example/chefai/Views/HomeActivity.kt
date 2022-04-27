package com.example.chefai.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chefai.PostData
import com.example.chefai.PostResponseData
import com.example.chefai.R
import com.example.chefai.ViewModel.HomeActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_actvity.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var viewModel: HomeActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_actvity)

        mAuth = FirebaseAuth.getInstance()

        initViewModel()

        generate.setOnClickListener {
            createPost()
        }
        logout.setOnClickListener {
            logoutUser()
        }
    }

    private fun createPost() {
        // creating a post
        val prompt =
            "Write a recipe based on these ingredients and instructions:\n\nFrito Pie\n\nIngredients:\nFritos\nChili\nShredded cheddar cheese\nSweet white or red onions, diced small\nSour cream\n\nInstructions:"
        val postdata = PostData(120, prompt, 0.3, 1.0, 0.0)
        viewModel.createPostData(postdata)
    }

    private fun initViewModel() {
        // initialize view model
        viewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        viewModel.getPostLiveObserver().observe(this, Observer<PostResponseData?> {
            if (it == null) {
                Toast.makeText(this@HomeActivity, "Failed to post data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "Successfully posted data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun logoutUser() {
        mAuth.signOut()
        updateUI()
    }

    private fun updateUI() {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}