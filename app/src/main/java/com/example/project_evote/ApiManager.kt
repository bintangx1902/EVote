package com.example.project_evote

import android.widget.Toast
import com.example.project_evote.Callback.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://e-vote.zaws.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)

    fun loginUser(username: String, password: String, callback: ApiCallback<String>) {
        val call: Call<LoginResponse> = authService.login(username, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.key
                    if (token != null) {
                        callback.onSuccess(token)
                    } else {
                        callback.onError("Token not received")
                    }
                } else {
                    callback.onError("Login failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onError(t.message ?: "An error occurred")
            }
        })
    }

    fun registerUser(
        csrf: String, username: String, password: String, confirm_password: String, email: String,
        first_name: String, last_name: String, phone: String, callback: ApiCallback<String>) {
        val call: Call<VoteCallback> = authService.register(csrf, username, email, first_name, last_name, password, confirm_password, phone)

        call.enqueue(object: Callback<VoteCallback> {
            override fun onResponse(call: Call<VoteCallback>, response: Response<VoteCallback>) {
                if (response.isSuccessful) {
                    val msg = response.body()?.msg
                    if (msg != null)
                        callback.onSuccess(msg)
                    else
                        callback.onError("No Messages, Failed to save the data")
                } else {
                    callback.onError("Regsiter Failed")
                }
            }

            override fun onFailure(call: Call<VoteCallback>, t: Throwable) {
                callback.onError(t.message ?: "An error occurred")
            }
        })

    }
}