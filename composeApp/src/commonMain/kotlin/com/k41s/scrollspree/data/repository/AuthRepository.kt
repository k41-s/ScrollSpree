package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.network.AuthApiService

class AuthRepository (
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    // TODO: Implement this and other Repositories

}