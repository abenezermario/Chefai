package com.example.chefai

import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()


        generate.setOnClickListener {
            createPost()
        }

    }

    private fun createPost() {
        val promptText = "Write a recipe based on these ingredients and instructions:\\n\\nFrito Pie\\n\\nIngredients:\\nFritos\\nChili\\nShredded cheddar cheese\\nSweet white or red onions, diced small\\nSour cream\\n\\nInstructions:"
        val params = PostData(max_tokens =  120, temperature = 0.3, prompt=promptText,top_p=1.0, presence_penalty=0.0  )
        viewModel.createPostData(params)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getPostLiveObserver().observe(this, Observer<PostResponseData?> {
            if (it == null) {
                Toast.makeText(this@MainActivity, "Failed to fetch", Toast.LENGTH_LONG).show()
            } else {
                var text = it!!.choices[0].text
                generated.text = text


                Toast.makeText(this@MainActivity, "Succesfully sent post", Toast.LENGTH_LONG).show()
            }
        })

    }
}