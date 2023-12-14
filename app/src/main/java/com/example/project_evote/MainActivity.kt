package com.example.project_evote

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.project_evote.Callback.ApiCallback

class MainActivity : AppCompatActivity() {
    private val apiManager = ApiManager()
    private lateinit var db :DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Network Connection!", Toast.LENGTH_SHORT).show()
        }


        val textRegister = findViewById<TextView>(R.id.textViewRegister)
        textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)

        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        btnLogin.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            if ((username != "") and (password != "")) {
                performLogin(username, password)
                val intent = Intent(this, Test::class.java)
                startActivity(intent)
            }

        }
    }
    fun performLogin(username: String, password: String) {
        apiManager.loginUser(username, password, object : ApiCallback<String> {
            override fun onSuccess(response: String) {
                // Save the token in SharedPreferences or wherever you prefer
                saveToken(response)
                // Handle other success scenarios
            }

            override fun onError(error: String) {
                // Handle login error
            }
        })
    }
    private fun saveToken(token: String) {
        db = DBHelper(this)
        val save_data = db.insertData(token)
        if (save_data)
            Toast.makeText(this, "Login Success!!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}