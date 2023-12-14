package com.example.project_evote.Callback

interface ApiCallback <T> {
    fun onSuccess(response: T)
    fun onError(error: String)
}