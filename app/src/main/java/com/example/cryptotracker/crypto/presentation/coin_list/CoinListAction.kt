package com.example.cryptotracker.crypto.presentation.coin_list

import com.example.cryptotracker.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClicked(val coinUi: CoinUi) : CoinListAction
    data object OnRefresh : CoinListAction
}