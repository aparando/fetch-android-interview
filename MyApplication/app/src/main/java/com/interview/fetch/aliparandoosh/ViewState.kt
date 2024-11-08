package com.interview.fetch.aliparandoosh

import com.interview.fetch.aliparandoosh.network.model.Item
import kotlinx.collections.immutable.ImmutableMap

sealed class ViewState {
    data class GetItemsSuccess(val itemsMap: ImmutableMap<Int, List<Item>>) : ViewState()
    data class GetItemsFailed(val message: String) : ViewState()
    data object Loading : ViewState()
    data object Init : ViewState()
}
