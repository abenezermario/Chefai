package com.example.chefai.Views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.chefai.PostData
import com.example.chefai.R
import com.example.chefai.ViewModel.HomeActivityViewModel
import com.example.chefai.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home_actvity.*


class HomeActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private val viewModel: HomeActivityViewModel by viewModels()
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(R.layout.activity_home_actvity)

        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser?.email.toString()
        // adding toggle to the nav bar
        toggle =
            ActionBarDrawerToggle(this@HomeActivity, drawer_layout, R.string.open, R.string.close)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setSupportActionBar(topAppBar)


        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> Toast.makeText(applicationContext, "profile", Toast.LENGTH_SHORT)
                    .show()
                R.id.settings -> Toast.makeText(applicationContext, "Settings", Toast.LENGTH_SHORT)
                    .show()
                R.id.logout -> logoutUser()
            }
            true
        }


        viewModel.createPostLiveData.observe(this) {
            if (it == null) {

                Toast.makeText(this@HomeActivity, "Failed to post data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "Successfully posted data", Toast.LENGTH_SHORT)
                    .show()

                openDialog(it.choices[0].text)
                Log.d("Instructions: ", it.choices[0].text)
            }

            progressBar.visibility = android.view.View.INVISIBLE
        }


        // add ingredients on enter
        ingredient.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            when {
                                TextUtils.isEmpty(
                                    ingredient.text.toString().trim { it <= ' ' }) -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "No input provided",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                else -> {
                                    val name = ingredient.text.toString()
                                    ingredient.setText("")
                                    addChipToGroup(name, chipGroup)
                                }
                            }
                            return true
                        }
                        else -> {}
                    }
                }
                return false
            }
        })

        generate.setOnClickListener {
            progressBar.visibility = android.view.View.VISIBLE
            val ingredients: String = getIngredients(chipGroup)
            createPost(ingredients)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("current", mAuth.currentUser?.email.toString())
        val currentUser: FirebaseUser? = mAuth.currentUser
        if(currentUser == null){
            mAuth.signOut()
        }
    }

    private fun openDialog(text: String) {

        val bundle = Bundle()
        bundle.putString("RESPONSE", text)
        bundle.putString("TITLE", recipeName.text.toString())


        val fragmentManager = supportFragmentManager
        val newFragment = RecipeFragment()

        newFragment.arguments = bundle

        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog")
        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    fun getIngredients(chipGroup: ChipGroup): String {
        val chipCount = chipGroup.childCount
        var ingredients: String = ""
        var i = 0
        while (i < chipCount) {
            var chip: Chip = chipGroup.getChildAt(i) as Chip
            ingredients += chip.text.toString() + "\n"
            i++
        }
        return ingredients

    }


    // mvvm
    private fun createPost(ingredient: String) {
        when {
            TextUtils.isEmpty(recipeName.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(applicationContext, "Recipe Name can't be empty", Toast.LENGTH_SHORT)
                    .show()

            }
            else -> {
                val recipeName = recipeName.text.toString().trim { it <= ' ' }
                val prompt =
                    "Write a recipe based on these ingredients and instructions:\n\n" + recipeName + "\n\nIngredients:\n" + ingredient + "\n\nInstructions:"
                val postdata = PostData(120, prompt, 0.3, 1.0, 0.0)
                viewModel.createPostData(postdata)
            }

        }
        // creating a post

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            Log.d("item", item.toString())
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}

