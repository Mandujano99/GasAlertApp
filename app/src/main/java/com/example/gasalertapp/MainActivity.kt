package com.example.gasalertapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class MainActivity : AppCompatActivity() {
    private var notificationId = 0
    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String) {
        // Create notification channel if needed (for Android 8.0 and above)
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(
            notificationId++,
            builder.build()
        ) // Use a unique notification ID
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Your Channel Name"
            val description = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance)
            channel.description = description

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val activityResultLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission(),
        object : ActivityResultCallback<Boolean?> {
            override fun onActivityResult(result: Boolean?) {
                if (result == true) {
                    Toast.makeText(
                        this@MainActivity,
                        "Post notification permission granted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.red)
        val database =
            FirebaseDatabase.getInstance("https://gasalert-c58b6-default-rtdb.firebaseio.com")
        val myRef = database.getReference("/Usuarios/usuario1")


        //myRef.setValue("Hello, World!");
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newData = snapshot.value
                Log.d(ContentValues.TAG, "Value is: $newData")
                if (newData != null) {
                    sendNotification(newData)
                } // Trigger notification with new data
                sendSMSMessage()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
            }
        })
        val myRef2 = database.getReference("/Usuarios/usuario1Pos")


        //myRef.setValue("Hello, World!");
        myRef2.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newData = snapshot.value
                Log.d(ContentValues.TAG, "Value is: $newData")
                if (newData != null) {
                    sendNotification2(newData)
                } // Trigger notification with new data
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        val btnEntrar = findViewById<Button>(R.id.btn_Entrar)
        val btnRegistrar = findViewById<Button>(R.id.btn_Registrarse)

        btnEntrar.setOnClickListener {
            val intent = Intent (this, Navigation::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent (this, Registro::class.java)
            startActivity(intent)
        }
    }
    private fun sendNotification(newData: Any) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val token = task.result
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(ContentValues.TAG, msg)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    // Construct notification payload
                    val payload: MutableMap<String, String> =
                        HashMap()
                    payload["title"] = "Data Changed"
                    payload["body"] = "New data: $newData" // Customize as needed

                    // Build and send FCM message
                    val message = RemoteMessage.Builder(token!!)
                        .setData(payload)
                        .build()
                    showNotification("Fuga", "Fuga detectada")
                    FirebaseMessaging.getInstance().send(message)
                    Log.d(ContentValues.TAG, "message send")
                } else {
                    Log.d(ContentValues.TAG, "Error")
                    // Handle token retrieval failure
                }
            }
    }

    private fun sendNotification2(newData: Any) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val token = task.result
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(ContentValues.TAG, msg)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    // Construct notification payload
                    val payload: MutableMap<String, String> =
                        HashMap()
                    payload["title"] = "Data Changed"
                    payload["body"] = "New data: $newData" // Customize as needed

                    // Build and send FCM message
                    val message = RemoteMessage.Builder(token!!)
                        .setData(payload)
                        .build()
                    showNotification("Alerta", "Cilindro no detectado")
                    FirebaseMessaging.getInstance().send(message)
                    Log.d(ContentValues.TAG, "message send")
                } else {
                    Log.d(ContentValues.TAG, "Error")
                    // Handle token retrieval failure
                }
            }
    }

    protected fun sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.SEND_SMS),
                    MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS as Int
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(
                        "6692225236",
                        null,
                        "SE HA DETECTADO UNA FUGA",
                        null,
                        null
                    )
                    Toast.makeText(
                        applicationContext, "SMS sent.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "SMS faild, please try again.", Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }
        }
    }

    companion object {
        val MY_PERMISSIONS_REQUEST_SEND_SMS: Any = 0
    }
}