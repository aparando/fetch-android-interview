package com.interview.fetch.aliparandoosh.repository

import com.interview.fetch.aliparandoosh.network.FetchApi
import com.interview.fetch.aliparandoosh.network.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemsRepository
@Inject constructor(private val fetchApi: FetchApi) {
    fun getItems(): Flow<List<Item>> = flow {
        emit(fetchApi.getItems())
    }
}
