package com.example.chefai.Views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import com.example.chefai.PostData
import com.example.chefai.R
import com.example.chefai.ViewModel.HomeActivityViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_actvity.*


class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_actvity)

        mAuth = FirebaseAuth.getInstance()


        viewModel.createPostLiveData.observe(this) {
            if (it == null) {

                Toast.makeText(this@HomeActivity, "Failed to post data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "Successfully posted data", Toast.LENGTH_SHORT)
                    .show()
                Log.d("Instructions: ", it.choices[0].text)
            }
        }
        ingredient.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {
                if(ingredient.text.toString().isNotEmpty())
                {

                    val name = ingredient.text.toString()
                    ingredient.setText("")
                    addChipToGroup(name, chipGroup)
                    return@setOnKeyListener true
                }
            }
            false
        }


        generate.setOnClickListener {
            val ingredients: String = getIngredients(chipGroup)
            createPost(ingredients)
        }
        logout.setOnClickListener {
            logoutUser()
        }
    }

    fun getIngredients(chipGroup: ChipGroup): String {
        val chipCount = chipGroup.childCount
        var ingredients: String = ""
        var i = 0
        while (i < chipCount)
        {
            var chip : Chip = chipGroup.getChildAt(i) as Chip
            ingredients += chip.text.toString() + "\n"
            i++
        }
        return ingredients

    }

    // mvvm
    private fun createPost(ingredient: String) {

        // creating a post
        val prompt =
            "Write a recipe based on these ingredients and instructions:\n\nPancake \n\nIngredients:\n" + ingredient + "\n\nInstructions:"
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
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
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

