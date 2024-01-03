package com.example.project_evote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class Test : AppCompatActivity() {
    private val baseUrl = "http://e-vote.zaws.net/"
    private lateinit var db: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {

        var token: String
        db = DBHelper(this)
        val t = db.checkToken()
        Log.d("Tag", "$t")
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

            val call: Call<VoteCallback> = apiService.postLogout(token, token)

            call.enqueue(object : Callback<VoteCallback> {
                override fun onResponse(call: Call<VoteCallback>, response: Response<VoteCallback>) {
                    Log.d("tag", "$response")
                    if (response.isSuccessful) {
                        val token = response.body()?.msg
                        db.deleteAllToken()
                        Toast.makeText(this@Test, token, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@Test, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@Test, "Logout Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<VoteCallback>, t: Throwable) {
                    Log.d("tag", "$t.message")
                    Toast.makeText(this@Test, "Network Error : ", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}