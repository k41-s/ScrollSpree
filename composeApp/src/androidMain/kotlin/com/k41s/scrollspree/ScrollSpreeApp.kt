package com.k41s.scrollspree

import android.app.Application
import com.k41s.scrollspree.di.appModule
import com.k41s.scrollspree.di.platformModule
import com.k41s.scrollspree.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ScrollSpreeApp : Application() {
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate() {
        super.onCreate()

        val koinApp = startKoin {
            androidContext(this@ScrollSpreeApp)
            modules(appModule, platformModule)
        }

        networkMonitor = koinApp.koin.get()
        networkMonitor.startMonitoring()
    }
}