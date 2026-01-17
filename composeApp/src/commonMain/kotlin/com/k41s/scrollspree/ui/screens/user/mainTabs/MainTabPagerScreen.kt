package com.k41s.scrollspree.ui.screens.user.mainTabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.navigation.UserNavigationActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabPagerScreen(
    actions: UserNavigationActions,
    onLogout: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 2, pageCount = {5})
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ScrollSpree") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val tabs = listOf(
                    Icons.Default.Info to 0,
                    Icons.Default.ShoppingCart to 1,
                    Icons.Default.Home to 2,
                    Icons.Default.Favorite to 3,
                    Icons.Default.Person to 4
                )

                tabs.forEach { (icon, index) ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        },
                        label = null,
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> FeatureComingSoonScreen("Info")
                1 -> FeatureComingSoonScreen("Cart")
                2 -> FeatureComingSoonScreen("HomeScreen")
                3 -> FeatureComingSoonScreen("Favorites")
                4 -> FeatureComingSoonScreen("Profile")
            }
        }
    }
}

@Composable
fun FeatureComingSoonScreen(featureName: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                "$featureName: Coming Soon",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}