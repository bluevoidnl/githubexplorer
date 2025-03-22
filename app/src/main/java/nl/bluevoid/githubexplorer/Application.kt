package nl.bluevoid.githubexplorer

import android.app.Application
import nl.bluevoid.githubexplorer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@Application)
            modules(appModule) // Load your Koin module
        }
    }
}