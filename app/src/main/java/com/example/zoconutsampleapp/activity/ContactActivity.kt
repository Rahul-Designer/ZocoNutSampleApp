package com.example.zoconutsampleapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.zoconutsampleapp.ContactAdapter
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.data.Contact
import com.example.zoconutsampleapp.databinding.ActivityContactBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContactActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContactBinding
    private lateinit var database : DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var arrContactList : ArrayList<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.contactRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.contactRecyclerview.setHasFixedSize(true)

        arrContactList = arrayListOf<Contact>()
        getContactData()

    }

    private fun getContactData() {
        database = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.currentUser?.uid.toString())

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Contact::class.java)
                        arrContactList.add(user!!)
                    }
                    binding.contactRecyclerview.adapter = ContactAdapter(applicationContext,arrContactList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}