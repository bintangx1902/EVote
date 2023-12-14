package com.example.project_evote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.project_evote.Callback.Candidate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CandidateList : AppCompatActivity() {
    private val baseUrl = "http://e-vote.zaws.net/"
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_list)

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
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(AuthService::class.java)
        val call = apiService.getCandidateList(token)

//        call.enqueue(object : Callback<List<Candidate>> {
//            override fun onResponse(call: Call<List<Candidate>>, response: Response<List<Candidate>>) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    findViewById<TextView>(R.id.leaderNameTextView).text = "Leader Name: $data"
//                } else {
//                    Toast.makeText(this@CandidateList, "Failed to get data", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Candidate>>, t: Throwable) {
//                Toast.makeText(this@CandidateList, "Network error", Toast.LENGTH_SHORT).show()
//            }
//        })
        call.enqueue(object : Callback<List<Candidate>> {
            override fun onResponse(call: Call<List<Candidate>>, response: Response<List<Candidate>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val vote1 = data.get(0).vote
                        findViewById<TextView>(R.id.hasil1).text = "Jumlah Suara : $vote1"

                        val vote2 = data.get(1).vote
                        findViewById<TextView>(R.id.hasil2).text = "Jumlah Suara : $vote2"

                        val vote3 = data.get(2).vote
                        findViewById<TextView>(R.id.hasil3).text = "Jumlah Suara : $vote3"

                    } else {
                        Toast.makeText(this@CandidateList, "Empty response body", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleApiError(response)
                }
            }

            override fun onFailure(call: Call<List<Candidate>>, t: Throwable) {
                handleNetworkError(t)
            }
        })
    }
    private fun handleApiError(response: Response<*>) {
        val errorMessage = "Failed to get data. Code: ${response.code()}"
        Log.e("CandidateList", errorMessage)
        Toast.makeText(this@CandidateList, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleNetworkError(t: Throwable) {
        val errorMessage = "Network error: ${t.message}"
        Log.e("CandidateList", errorMessage, t)
        Toast.makeText(this@CandidateList, errorMessage, Toast.LENGTH_SHORT).show()
    }
}