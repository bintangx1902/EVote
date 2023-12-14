package com.example.project_evote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VoteActivity : AppCompatActivity() {
    private val baseUrl = "http://e-vote.zaws.net/"
    private lateinit var db : DBHelper

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService: AuthService = retrofit.create(AuthService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

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
        Log.d("tag", token)
        // vote paslon 1
        findViewById<Button>(R.id.btn1).setOnClickListener {
            postVote(token, "1", "1")
        }

        // vote paslon 2
        findViewById<Button>(R.id.btn2).setOnClickListener {
            postVote(token, "2", "1")
        }

        // vote paslon 3
        findViewById<Button>(R.id.btn3).setOnClickListener {
            postVote(token, "3", "1")
        }

    }
    private fun postVote(authorization: String, candidateId: String, vote: String) {
        val call = apiService.postVote(authorization, candidateId, vote)

        call.enqueue(object : Callback<VoteCallback> {
            override fun onResponse(call: Call<VoteCallback>, response: Response<VoteCallback>) {
                when (response.code()) {
                    201 -> {
                        val msg = response.body()?.msg
                        Toast.makeText(this@VoteActivity, "Vote berhasil: $msg", Toast.LENGTH_LONG).show()
                    }
                    403 -> {
                        Toast.makeText(this@VoteActivity, "Kamu sudah Voting", Toast.LENGTH_LONG).show()
                    }
                    404 -> {
                        Toast.makeText(this@VoteActivity, "404 Not Found", Toast.LENGTH_LONG).show()
                    }
                    406 -> {
                        Toast.makeText(this@VoteActivity, "Vote tidak valid", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        // Handle other response codes if needed
                    }
                }
            }

            override fun onFailure(call: Call<VoteCallback>, t: Throwable) {
                // Handle network errors
                Toast.makeText(this@VoteActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
        val intent = Intent(this, Test::class.java)
        startActivity(intent)
    }
}