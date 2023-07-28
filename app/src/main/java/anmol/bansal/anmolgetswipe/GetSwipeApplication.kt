package anmol.bansal.anmolgetswipe

import android.app.Application
import anmol.bansal.anmolgetswipe.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class GetSwipeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GetSwipeApplication)
            modules(appModule)
        }
    }
}
