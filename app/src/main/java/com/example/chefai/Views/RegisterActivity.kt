package com.example.chefai.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.chefai.R
import com.example.chefai.dto.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        mAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            when {
                TextUtils.isEmpty(registerEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Invalid Input email", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(registerPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Invalid Password", Toast.LENGTH_SHORT)
                        .show()

                }
                else -> {
                    val email: String = registerEmail.text.toString().trim { it <= ' ' }
                    val password: String = registerPassword.text.toString().trim { it <= ' ' }
                    // creating user with email and password
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { it ->
                            if (it.isSuccessful) {

                                Log.d("userInfo", mAuth.currentUser!!.uid)
                                val firebaseUser: FirebaseUser = it.result.user!!

                                val userInfo = UserData(email, password)
                                // writing to firebase database

                                // Write a message to the database
                                val database = Firebase.database
                                val myRef = database.getReference("USERINFO")
                                    .child("USERINFO").setValue(userInfo)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Log.d("database", "Registered to db")
                                        } else {
                                            Log.d("failedReg", it.exception?.message.toString())
                                        }
                                    }


                                progress_circular.visibility = android.view.View.VISIBLE
                                var intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                startActivity(intent)
                                finish()

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("succAuth", "Successfully Authenticated")
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    it.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("failedAUth", "Failed Authentication")
                            }
                        }
                }


            }


        }
    }

    private fun formatLogin(email: Editable?, password: Editable?) {

        TextUtils.isEmpty(email.toString().trim(' '))
        TextUtils.isEmpty(password.toString().trim(' '))


    }
}