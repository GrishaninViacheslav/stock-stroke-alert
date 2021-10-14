package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.grishaninvyacheslav.stock_stroke_alert.BuildConfig
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage.IAlphaVantageDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AlphaVantageApiModule {

    @Named("baseUrl")
    @Provides
    fun baseUrl(): String = "https://www.alphavantage.co/"

    @Provides
    fun provideTickerDataApi(@Named("baseUrl") baseUrl: String, gson: Gson): IAlphaVantageDataSource =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AlphaVantageApiInterceptor)
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IAlphaVantageDataSource::class.java)

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}

object AlphaVantageApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response =
            chain.proceed(
                chain.request()
                    .newBuilder()
                    // TODO: Не могу понять почему не добавляется header
                    .header("apikey", BuildConfig.ALPHA_VANTAGE_API_KEY)
                    .build()
            )
        // TODO: header apikey отсутствует в запросе
        Log.d("[Interceptor]", "requestUrl: ${response.request().url()}")
        return response
    }
}