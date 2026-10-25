package com.tets.manhwa

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.tets.manhwa.data.local.AppDatabase
import com.tets.manhwa.data.remote.TetsApi
import com.tets.manhwa.data.remote.TetsApiService
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.ui.navigation.TetsNavHost
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val appContext: android.content.Context) {
    val repository: TetsRepository by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(TetsApiService::class.java)
        val db = AppDatabase.getInstance(appContext)
        TetsRepository(api, db)
    }
}

@Composable
fun TetsApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    TetsNavHost(navController = navController, repository = appContainer.repository)
}
