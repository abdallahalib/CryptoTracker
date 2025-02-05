package com.example.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotracker.core.presentation.util.ObserveAsEvent
import com.example.cryptotracker.core.presentation.util.toString
import com.example.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinListDetailPane(
    viewModel: CoinListViewModel = koinViewModel(),
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvent(events = viewModel.events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(context, event.error.toString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    state = state,
                    onAction = { action ->
                     viewModel.onAction(action)
                     when (action) {
                         is CoinListAction.OnCoinClicked -> navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                     }
                    },
                    modifier = modifier
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CoinDetailScreen(
                    state = state,
                    modifier = modifier
                )
            }
        }

    )
}