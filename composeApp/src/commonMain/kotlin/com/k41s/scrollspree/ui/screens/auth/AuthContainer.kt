package com.k41s.scrollspree.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.screens.auth.login.LoginScreen
import com.k41s.scrollspree.ui.screens.auth.register.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun AuthContainer() {

    val pagerState = rememberPagerState(pageCount = {2})
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) { page ->
        when (page) {
            0 -> LoginScreen(
                onNavigateToRegister = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
            1 -> RegisterScreen(
                onNavigateToLogin = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            )
        }
    }
}