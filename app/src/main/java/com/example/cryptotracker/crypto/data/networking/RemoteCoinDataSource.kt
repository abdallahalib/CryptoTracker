package com.example.cryptotracker.crypto.data.networking

import com.example.cryptotracker.core.domain.util.Result
import com.example.cryptotracker.core.data.networking.safeCall
import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.map
import com.example.cryptotracker.crypto.data.mapper.toCoin
import com.example.cryptotracker.crypto.data.mapper.toCoinPrice
import com.example.cryptotracker.crypto.data.networking.dto.CoinHistoryDto
import com.example.cryptotracker.crypto.data.networking.dto.CoinsResponseDto
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.domain.CoinDataSource
import com.example.cryptotracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient,
): CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get("assets")
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMs = start.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val endMs = end.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        return safeCall<CoinHistoryDto> {
            httpClient.get("assets/$coinId/history") {
                parameter("interval", "h6")
                parameter("start", startMs)
                parameter("end", endMs)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}