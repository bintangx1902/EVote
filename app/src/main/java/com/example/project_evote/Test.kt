package com.example.project_evote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Test : AppCompatActivity() {
    private val baseUrl = "https://e-vote.zaws.net/"
    private lateinit var db: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {

        var token: String
        db = DBHelper(this)
        val t = db.checkToken()
        if (t.isNotEmpty())
            token = "Token $t"
        else {
            Toast.makeText(this, "Gagal Login!", Toast.LENGTH_SHORT).show()
            token = ""
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        findViewById<Button>(R.id.btnVote).setOnClickListener {
            val intent = Intent(this, VoteActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnQuickCount).setOnClickListener {
            val intent = Intent(this, CandidateList::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(AuthService::class.java)

            val call = apiService.postLogout(token)

            call.enqueue(object : Callback<VoteCallback> {
                override fun onResponse(call: Call<VoteCallback>, response: Response<VoteCallback>) {
                    if (response.isSuccessful) {
                        val message = response.body()?.msg ?: "Unknown response"
                        Toast.makeText(this@Test, message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Test, "Logout Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<VoteCallback>, t: Throwable) {
                    Toast.makeText(this@Test, "Network Error", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}