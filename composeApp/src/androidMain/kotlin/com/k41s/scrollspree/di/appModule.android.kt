package com.k41s.scrollspree.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.k41s.scrollspree.data.local.DATA_STORE_FILE_NAME
import com.k41s.scrollspree.data.local.createDataStore
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

actual val platformModule = module {
    single {
        createDataStore(androidContext())
    }
}

fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore(
        producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
    )
}