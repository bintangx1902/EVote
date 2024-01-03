package com.example.project_evote

import com.example.project_evote.Callback.Candidate
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @FormUrlEncoded
    @POST("/rest-auth/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("/api/candidate")
    fun getCandidateList(@Header("Authorization") token: String): Call<List<Candidate>>

    @FormUrlEncoded
    @POST("/api/vote")
    fun postVote(
//        @Header("Authorization") token: String,
//        @Field("candidate_id") candidate_id: Int,
//        @Field("vote") vote: Int
        @Header("Authorization") authorization: String,
        @Field("candidate_id") candidateId: String,
        @Field("vote") vote: String
    ): Call<VoteCallback>

    @FormUrlEncoded
    @POST("/rest-auth/logout/")
    fun postLogout(@Header("Authorization") token: String,
                    @Field("key") key: String): Call<VoteCallback>

    @FormUrlEncoded
    @POST("/api/register")
    fun register(
        @Header("X-CSRFToken") csrf: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("phone") phone: String
    ): Call<VoteCallback>

}