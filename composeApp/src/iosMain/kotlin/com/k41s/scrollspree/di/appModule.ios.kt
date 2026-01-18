package com.k41s.scrollspree.di

import coil3.PlatformContext
import com.k41s.scrollspree.data.local.DATA_STORE_FILE_NAME
import com.k41s.scrollspree.data.local.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual val platformModule = module {
    single { PlatformContext.INSTANCE }

    single {
        createDataStore {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            requireNotNull(documentDirectory?.path) + "/$DATA_STORE_FILE_NAME"
        }
    }
}