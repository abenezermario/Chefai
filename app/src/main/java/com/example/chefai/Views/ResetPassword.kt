package com.example.chefai.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.chefai.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.email
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var resetPassword: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance()

        val resetPassword = findViewById<Button>(R.id.resetPassword)
        resetPassword.setOnClickListener {

            reset()
        }
    }

    private fun reset() {
        when {
            TextUtils.isEmpty(resetEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Invalid email input", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val email = resetEmail.text.toString().trim { it <= ' ' }
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Reset Link Sent", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}