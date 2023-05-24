package com.example.zoconutsampleapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.databinding.ActivityQrscannerBinding
import com.google.zxing.integration.android.IntentIntegrator

class QRScannerActivity : AppCompatActivity() {
    lateinit var binding: ActivityQrscannerBinding
    private var qrScanIntegrator: IntentIntegrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrscanner)

        qrScanIntegrator?.setOrientationLocked(false)

        setOnClickListener()
        setupScanner()
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun setOnClickListener() {
        binding.qrScanBtn.setOnClickListener {
            performAction()
        }
    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                val resultText = result.contents.toString()
                val data : List<String> = resultText.split("\n")
//                binding.qrtext.text = resultText
                val intent = Intent(this,CardViewerActivity::class.java)
                intent.putStringArrayListExtra("DATA",ArrayList<String>(data))
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}