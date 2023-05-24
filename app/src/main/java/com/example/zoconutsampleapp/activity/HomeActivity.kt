package com.example.zoconutsampleapp.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageref: FirebaseStorage
    private val multiFormatWriter = MultiFormatWriter()
    lateinit var mBitmap: Bitmap
    private lateinit var uri: Uri
    private lateinit var imageUrl: String
    var click: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        firebaseAuth = FirebaseAuth.getInstance()
        storageref = FirebaseStorage.getInstance()

        binding.qrScanner.setOnClickListener {
            startActivity(Intent(it.context, QRScannerActivity::class.java))
        }

        binding.contact.setOnClickListener {
            startActivity(Intent(it.context, ContactActivity::class.java))
        }
        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.profileImg.setImageURI(it)
                uri = it
            })

        binding.profileImg.setOnClickListener {
            click = true
            galleryImage.launch("image/*")
        }

        binding.qrGenerateBtn.setOnClickListener {
            try {
                if (click) {
                    val progressDialog = ProgressDialog(this@HomeActivity)
                    progressDialog.setTitle("Please Wait..")
                    progressDialog.setMessage("Application is loading, please wait")
                    progressDialog.show()
                    val reference =
                        storageref.reference.child("Profile").child(Date().time.toString())
                    reference.putFile(uri).addOnCompleteListener {
                        if (it.isSuccessful) {
                            reference.downloadUrl.addOnSuccessListener {
                                progressDialog.dismiss()
                                imageUrl = it.toString()
                                generateQR()
                            }

                        }
                    }
                }
                else{
                    Snackbar.make(it,"Please Upload Profile Picture",Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        binding.shareQrBtn.setOnClickListener {
            val path = MediaStore.Images.Media.insertImage(
                contentResolver,
                mBitmap,
                "Image Description",
                null
            )
            val uri = Uri.parse(path)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Image"))
        }

//        getFirebaseData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.logout) {
            firebaseAuth.signOut()
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putBoolean("flag", false)
            editor?.apply()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateQR() {
        val userId = firebaseAuth.currentUser?.uid
        val profileImg = imageUrl
        val name = binding.nameField.editText?.text.toString()
        val email = binding.emailField.editText?.text.toString()
        val phoneNumber = binding.phoneNumberField.editText?.text.toString()
        val githubLink = binding.githubProfileField.editText?.text.toString()
        val listOfSkill = binding.skillField.editText?.text.toString()
        val city = binding.cityField.editText?.text.toString()
        val country = binding.countryField.editText?.text.toString()
        val myText =
            "$userId\n$profileImg\n$name\n$email\n$phoneNumber\n$githubLink\n$listOfSkill\n$city\n$country"
        //BitMatrix class to encode entered text and set Width & Height
        val mMatrix: BitMatrix =
            multiFormatWriter.encode(myText, BarcodeFormat.QR_CODE, 200, 200)
        val mEncoder = BarcodeEncoder()
        mBitmap = mEncoder.createBitmap(mMatrix) //creating bitmap of code
        binding.idIVQrcode.setImageBitmap(mBitmap) //Setting generated QR code to imageView
    }
}