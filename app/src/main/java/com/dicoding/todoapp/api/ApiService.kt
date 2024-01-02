package com.dicoding.todoapp.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("v1/ip/geo.json")
    fun getGeo():Call<GetGeoResponse>
}