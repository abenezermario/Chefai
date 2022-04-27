package com.example.chefai.Views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chefai.PostData
import com.example.chefai.R
import com.example.chefai.ViewModel.HomeActivityViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.color.DynamicColors
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_actvity.*
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_actvity)
        DynamicColors.applyIfAvailable(this)
        mAuth = FirebaseAuth.getInstance()

//        initViewModel()

        viewModel.createPostLiveData.observe(this) {
            if (it == null) {
                Toast.makeText(this@HomeActivity, "Failed to post data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "Successfully posted data", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        ingredient.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                Log.d("done", v.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
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
            "Write a recipe based on these ingredients and instructions:\n\nFrito Pie\n\nIngredients:\nFritos\nChili\nShredded cheddar cheese\nSweet white or red onions, diced small\nSour creamFrito Pie\n\nIngredients:\nFritos\nChili\nShredded cheddar cheese\nSweet white or red onions, diced small\nSour cream\n\nInstructions:"
        val postdata = PostData(120, prompt, 0.3, 1.0, 0.0)
        viewModel.createPostData(postdata)
    }

    private fun addChipToGroup(ingredient: String, chipGroup: ChipGroup) {
        val chip = Chip(this)
        chip.text = ingredient
        chip.isCloseIconVisible = true
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        Log.d("added", "CHIP ADDED")
        chip.setOnCloseIconClickListener {chipGroup.removeView(chip as View)}
    }
//
//    private fun initViewModel() {
//        // initialize view model
//        viewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
//        viewModel.getPostLiveObserver().observe(this, Observer<PostResponseData?> {
//            if (it == null) {
//                Toast.makeText(this@HomeActivity, "Failed to post data", Toast.LENGTH_SHORT).show()
//            } else {
//                Log.d("ress", it.toString())
//                Toast.makeText(this@HomeActivity, "Successfully posted data", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
//    }

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

