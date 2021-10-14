package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.github.grishaninvyacheslav.stock_stroke_alert.BuildConfig
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Quote
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query

interface IAlphaVantageDataSource {
    @GET("query?function=SYMBOL_SEARCH")
    fun symbolSearch(
        @Query("keywords") keywords: String,
        // TODO: пробовал использовать Interceptor - AlphaVantageApiInterceptor(в AlphaVantageApiModule),
        //             но он почему то не добавляет header для apikey
        @Query("apikey") apikey: String = BuildConfig.ALPHA_VANTAGE_API_KEY
    ): Single<Map<String, List<Ticker>>>

    @GET("query?function=TIME_SERIES_INTRADAY")
    fun intraday(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("outputsize") outputsize: String = "compact",
        @Query("apikey") apikey: String = BuildConfig.ALPHA_VANTAGE_API_KEY
    ): Single<AVIntradayResponseDTO>

    @GET("query?function=GLOBAL_QUOTE")
    fun globalQuote(
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String = BuildConfig.ALPHA_VANTAGE_API_KEY
    ): Single<Map<String, GlobalQuote>>
}

@Parcelize
data class AVIntradayResponseDTO(
    @SerializedName("Meta Data")
    @Expose
    val metadata: AVIntradayMetadata,


    // TODO:
    @SerializedName("Time Series (5min)")
    @Expose
    val timeSeries: Map<String, Quote>
) : Parcelable

@Parcelize
data class AVIntradayMetadata(
    @SerializedName("1. Information")
    @Expose
    val information: String,

    @SerializedName("2. Symbol")
    @Expose
    val symbol: String,

    @SerializedName("3. Last Refreshed")
    @Expose
    val lastRefreshed: String,

    @SerializedName("4. Interval")
    @Expose
    val interval: String,

    @SerializedName("5. Output Size")
    @Expose
    val outputSize: String,

    @SerializedName("6. Time Zone")
    @Expose
    val timeZone: String
) : Parcelable

@Parcelize
data class GlobalQuote(
    @SerializedName("01. symbol")
    @Expose
    val symbol: String,

    @SerializedName("02. open")
    @Expose
    val open: String,

    @SerializedName("03. high")
    @Expose
    val high: String,

    @SerializedName("04. low")
    @Expose
    val low: String,

    @SerializedName("05. price")
    @Expose
    val price: String,

    @SerializedName("06. volume")
    @Expose
    val volume: String,

    @SerializedName("07. latest trading day")
    @Expose
    val latestTradingDay: String,

    @SerializedName("08. previous close")
    @Expose
    val previousClose: String,

    @SerializedName("09. change")
    @Expose
    val change: String,

    @SerializedName("10. change percent")
    @Expose
    val changePercent: String
) : Parcelable
