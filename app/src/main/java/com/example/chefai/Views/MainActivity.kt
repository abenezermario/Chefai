package com.example.chefai.Views

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chefai.R
import com.google.android.material.color.DynamicColors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DynamicColors.applyIfAvailable(this)
        mAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            loginUser()
        }

        resetPassword.setOnClickListener {
            val intent = Intent(this@MainActivity, ResetPassword::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@MainActivity, "Invalid email address", Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@MainActivity, "Invalid Password", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                val email = email.text.toString().trim { it <= ' ' }
                val password = password.text.toString().trim { it <= ' ' }
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
//                            progress_circular.visibility = android.view.View.VISIBLE
                            Toast.makeText(
                                this@MainActivity,
                                "Login sucess",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val user = mAuth.currentUser
                            updateUI(user!!)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            updateUI(null!!)
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser) {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}