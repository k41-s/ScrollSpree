package com.k41s.scrollspree

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.k41s.scrollspree.di.appModule
import com.k41s.scrollspree.di.platformModule
import com.k41s.scrollspree.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : FragmentActivity() {
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule, platformModule)
        }

        networkMonitor = NetworkMonitor(this)
        networkMonitor.startMonitoring()

        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.stopMonitoring()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}