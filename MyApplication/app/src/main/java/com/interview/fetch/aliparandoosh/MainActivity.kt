package com.interview.fetch.aliparandoosh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.interview.fetch.aliparandoosh.network.model.Item
import com.interview.fetch.aliparandoosh.ui.theme.MyApplicationTheme
import com.interview.fetch.aliparandoosh.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableMap
import kotlin.collections.component1
import kotlin.collections.component2

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val modifier = Modifier.fillMaxSize()
                Scaffold(modifier = modifier) { innerPadding ->
                    MainContent(modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun MainContent(modifier: Modifier = Modifier) {
        val viewState = viewModel.viewStateFlow.collectAsStateWithLifecycle(ViewState.Init).value
        when (viewState) {
            ViewState.Init -> {}
            ViewState.Loading -> {
                LoadingContent(modifier)
            }

            is ViewState.GetItemsFailed -> {
                ErrorContent(viewState.message, modifier)
            }

            is ViewState.GetItemsSuccess -> {
                ItemListWithHeader(viewState.itemsMap, modifier)
            }
        }
    }

    @Composable
    fun LoadingContent(modifier: Modifier = Modifier) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(dimensionResource(R.dimen.progress_indicator_size)))
        }
    }

    @Composable
    fun ErrorContent(errorMessage: String, modifier: Modifier = Modifier) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                text = stringResource(R.string.api_error_message, errorMessage)
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemListWithHeader(
        groupedItems: ImmutableMap<Int, List<Item>>,
        modifier: Modifier = Modifier
    ) {
        LazyColumn(modifier) {
            groupedItems.forEach { (header, items) ->
                stickyHeader {
                    ListIdHeader(header)
                }
                items(
                    items = items,
                    key = { item -> item.id }
                ) { item ->
                    ItemRow(item)
                }
            }
        }
    }

    @Composable
    fun ListIdHeader(header: Int) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(dimensionResource(R.dimen.default_padding)),
            text = "$header",
            style = Typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
        )
    }

    @Composable
    fun ItemRow(item: Item) {
        Text(
            text = "${item.name}",
            modifier = Modifier.padding(dimensionResource(R.dimen.default_padding)),
            style = Typography.headlineSmall
        )
        HorizontalDivider()
    }
}
