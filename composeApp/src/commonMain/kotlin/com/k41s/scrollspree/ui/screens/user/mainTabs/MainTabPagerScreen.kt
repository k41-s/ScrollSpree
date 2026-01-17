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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.navigation.UserNavigationActions
import kotlinx.coroutines.launch

@Composable
fun MainTabPagerScreen(
    actions: UserNavigationActions
) {
    val pagerState = rememberPagerState(initialPage = 2, pageCount = {5})
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val tabs = listOf(
                    Triple(Icons.Default.Info, "Info", 0),
                    Triple(Icons.Default.ShoppingCart, "Cart", 1),
                    Triple(Icons.Default.Home, "Home", 2),
                    Triple(Icons.Default.Favorite, "Faves", 3),
                    Triple(Icons.Default.Person, "Profile", 4)
                )

                tabs.forEach { (icon, label, index) ->
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
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(label)
                        }
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