package com.k41s.scrollspree.ui.screens

sealed class Screen(val root: String) {
    data object UserEntry : Screen("user_entry")
    data object AdminEntry : Screen("admin_entry")
    data object Login : Screen("login")
}