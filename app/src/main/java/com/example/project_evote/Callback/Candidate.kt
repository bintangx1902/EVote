package com.example.project_evote.Callback

data class Candidate(
    val id: Int,
    val leader: String,
    val candidate: String,
    val vote: Int,
    val co_leader: String
)
