package com.example.zoconutsampleapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.data.Contact
import com.example.zoconutsampleapp.databinding.ActivityCardViewerBinding
import com.google.firebase.database.FirebaseDatabase

class CardViewerActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityCardViewerBinding
    private lateinit var userId: String
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_viewer)

        database = FirebaseDatabase.getInstance()

        val intent = getIntent()
        val data = intent.getStringArrayListExtra("DATA")
        userId = data?.get(0).toString()
        Glide.with(this).load(data?.get(1).toString()).into(binding.profileImg)
        userName = data?.get(2).toString()
        binding.nameField.text = userName
        binding.emailField.text = data?.get(3).toString()
        binding.phoneNumberField.text = data?.get(4).toString()
        binding.githubProfileField.text = data?.get(5).toString()
        binding.skillField.text = data?.get(6).toString()
        binding.cityField.text = data?.get(7).toString()
        binding.countryField.text = data?.get(8).toString()

        val contact = Contact(
            userId,
            data?.get(1).toString(),
            data?.get(2).toString(),
            data?.get(3).toString(),
            data?.get(4).toString(),
            data?.get(5).toString(),
            data?.get(6).toString(),
            data?.get(7).toString(),
            data?.get(8).toString()
        )

        binding.saveContactBtn.setOnClickListener {
            uploadData(contact)
        }
    }

    private fun uploadData(contact: Contact) {

        database.reference.child("users")
            .child(userId)
            .child(userName)
            .setValue(contact)
            .addOnSuccessListener {
                startActivity(Intent(this, ContactActivity::class.java))
                finish()
            }
    }
}