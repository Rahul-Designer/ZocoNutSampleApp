package com.example.zoconutsampleapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            val name = binding.nameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            val confirmPassword = binding.confirmPasswordEdt.text.toString()
            if (name.isEmpty()) binding.emailEdt.error = "Please enter name"
            if (email.isEmpty()) binding.emailEdt.error = "Please enter email"
            if (password.isEmpty()) binding.passwordEdt.error = "Please enter password"
            if (password != confirmPassword) binding.passwordEdt.error =
                "Please enter same password"
            if (isValidPassword(password)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Shared Preference
                            val pref = getSharedPreferences("login", MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putBoolean("flag", true)
                            editor.apply()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            } else {
                binding.passwordEdt.error = "Please enter valid password "
            }
        }

        binding.googleSignUpBtn.setOnClickListener {
            signInGoogle()
        }

    }


    // Google SignUp Code
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


    // Password Validation Code
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.firstOrNull { it.isDigit() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.firstOrNull { !it.isLetterOrDigit() } == null) return false
        return true
    }
}