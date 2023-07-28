package anmol.bansal.anmolgetswipe.di

import android.content.Context
import android.net.ConnectivityManager
import anmol.bansal.anmolgetswipe.BuildConfig
import anmol.bansal.anmolgetswipe.data.network.ProductApi
import anmol.bansal.anmolgetswipe.data.repository.MainRepository
import anmol.bansal.anmolgetswipe.data.repository.MainRepositoryImpl
import anmol.bansal.anmolgetswipe.ui.fragments.addProduct.AddProductViewModel
import anmol.bansal.anmolgetswipe.ui.fragments.productList.ProductListViewModel
import anmol.bansal.anmolgetswipe.utils.DispatcherProvider
import anmol.bansal.anmolgetswipe.utils.DispatcherProviderImpl
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
            .create(ProductApi::class.java)
    }

    single<MainRepository> { MainRepositoryImpl(get(), get()) }

    single { provideConnectivityManager(get()) }

    single<DispatcherProvider> { DispatcherProviderImpl() }

    viewModel { ProductListViewModel(get(), get()) }
    viewModel { AddProductViewModel(get(), get()) }
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().build()
}

private fun provideConnectivityManager(context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}