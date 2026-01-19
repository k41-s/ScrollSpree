package com.k41s.scrollspree.ui.screens.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.components.FeatureComingSoonScreen
import com.k41s.scrollspree.ui.screens.admin.category.AdminCategoryContainer
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainContainer(
    onLogout: () -> Unit
) {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = {4})
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
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
                    Triple(Icons.AutoMirrored.Filled.List, "Categories", 0),
                    Triple(Icons.Default.Flag, "Countries", 1),
                    Triple(Icons.Default.Build, "Products", 2),
                    Triple(Icons.Default.Person, "Users", 3)
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
                        label = { Text(label) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> AdminCategoryContainer()
                1 -> FeatureComingSoonScreen("Countries CRUD")
                2 -> FeatureComingSoonScreen("Products CRUD")
                3 -> FeatureComingSoonScreen("Users & Orders")
            }
        }
    }
}