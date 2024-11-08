package com.interview.fetch.aliparandoosh.network.model

import androidx.compose.runtime.Immutable

@Immutable
data class Item(
    val id: Int,
    val listId: Int,
    val name: String?
)
