package com.k41s.scrollspree.ui.main

sealed class AppViewState {
    data object Loading: AppViewState()
    data object Unauthorized: AppViewState()
    data object UserAuthenticated: AppViewState()
    data object AdminAuthenticated: AppViewState()
}