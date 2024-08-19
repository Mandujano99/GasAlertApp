package com.example.gasalertapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        window.statusBarColor = ContextCompat.getColor(this, R.color.red)

        val btnEntra = findViewById<Button>(R.id.btn_entra)


        btnEntra.setOnClickListener {
            val intent = Intent (this, Navigation::class.java)
            startActivity(intent)
        }
    }
}