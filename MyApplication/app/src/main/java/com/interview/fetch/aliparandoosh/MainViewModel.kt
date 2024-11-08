package com.interview.fetch.aliparandoosh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.fetch.aliparandoosh.network.model.Item
import com.interview.fetch.aliparandoosh.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val repository: ItemsRepository) : ViewModel() {

    private val _viewStateFlow: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Init)
    val viewStateFlow: Flow<ViewState> = _viewStateFlow.asStateFlow()

    init {
        getItems()
    }

    private fun getItems() {
        viewModelScope.launch {
            repository.getItems()
                .flowOn(Dispatchers.IO)
                .onStart { _viewStateFlow.value = ViewState.Loading }
                .catch { error ->
                    _viewStateFlow.value = ViewState.GetItemsFailed(error.message ?: "")
                }
                .collect { items ->
                    val processedItems = items
                        .filterNot { it.name.isNullOrBlank() }
                        .sortedWith(compareBy(Item::listId, Item::name))
                        .groupBy { it.listId }
                        .toImmutableMap()
                    _viewStateFlow.value = ViewState.GetItemsSuccess(processedItems)
                }
        }
    }
}
