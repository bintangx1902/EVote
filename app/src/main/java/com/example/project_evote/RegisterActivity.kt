package com.example.project_evote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.project_evote.Callback.ApiCallback

class RegisterActivity : AppCompatActivity() {
    private val apiManager = ApiManager()
    private var csrf = "WmQE0q3nHFfdmeFop55VqyXMrnVjlV5jmTWOBPfEzGFhSzdZVCN7zMpdinuBnC7p"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // click to login
        val textLogin = findViewById<TextView>(R.id.textViewLoginInstead)
        textLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // get data
        val f_name = findViewById<EditText>(R.id.editTextNamaDepan)
        val l_name = findViewById<EditText>(R.id.editTextNamaBelakang)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val cpassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val username = findViewById<EditText>(R.id.editTextNIK)
        val phone = findViewById<EditText>(R.id.editTextNoHP)
        val email = findViewById<TextView>(R.id.editTextEmail)

        findViewById<TextView>(R.id.textViewLoginInstead).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            val f_name = f_name.text.toString()
            val l_name = l_name.text.toString()
            val username = username.text.toString()
            val password = password.text.toString()
            val con_pass = cpassword.text.toString()
            val phone = phone.text.toString()
            val email = email.text.toString()

            if (f_name.isNotEmpty() and l_name.isNotEmpty() and username.isNotEmpty() and password.isNotEmpty()
                and con_pass.isNotEmpty() and phone.isNotEmpty()
            ) {
                if (con_pass != password) {
                    Toast.makeText(this, "Password tidak sama!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, this::class.java))
                }
                performRegister(username, password, con_pass, email, f_name, l_name, phone)
            }
        }

    }
    fun performRegister(
        username: String, password: String, confirm_password: String, email: String,
        first_name: String, last_name: String, phone: String) {
        apiManager.registerUser(
            csrf = csrf,
            username = username,
            email = email,
            password = password,
            confirm_password = confirm_password,
            first_name = first_name,
            last_name = last_name,
            phone = phone,
            callback = object: ApiCallback<String> {
                override fun onSuccess(response: String) {
                    makeToast("Berhasil Registrasi")
                }

                override fun onError(error: String) {
                    makeToast("Registrasi Gagal")
                }
            })
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}