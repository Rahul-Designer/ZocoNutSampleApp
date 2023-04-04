package com.example.zoconutsampleapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignIn : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        // Shared Preference Code
        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val check = pref.getBoolean("flag", false)
        if (check) {
            // for true Login (User already login )
            startActivity(Intent(this@SignIn, HomeActivity::class.java))
            finish()
        }

        // Sign with Email I'd and Password
        binding.signInBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            if (email.isEmpty()) binding.emailEdt.error = "Please enter email"
            if (password.isEmpty()) binding.passwordEdt.error = "Please enter password"
            else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Shared Preference
                        val pref = getSharedPreferences("login", MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putBoolean("flag", true)
                        editor.apply()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            binding.emailEdt.text = null
            binding.passwordEdt.text = null
        }

        // Google Sign Option
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignUpBtn.setOnClickListener {
            signInGoogle()
        }
    }

    // Google Sign Option

    private fun signInGoogle() {
        val signInIntent = this.googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // Shared Preference
                val pref = getSharedPreferences("login", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean("flag", true)
                editor.apply()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }


}