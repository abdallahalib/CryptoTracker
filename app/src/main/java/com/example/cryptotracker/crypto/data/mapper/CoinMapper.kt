package com.example.cryptotracker.crypto.data.mapper

import com.example.cryptotracker.crypto.data.networking.dto.CoinDto
import com.example.cryptotracker.crypto.domain.Coin

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        symbol = symbol,
        name = name,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
    )
}