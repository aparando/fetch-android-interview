package com.interview.fetch.aliparandoosh.network

import com.interview.fetch.aliparandoosh.network.model.Item
import retrofit2.http.GET

interface FetchApi {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}
