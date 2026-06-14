package com.k41s.scrollspree.ui.screens.user.mainTabs.profile

import com.k41s.scrollspree.domain.model.User

data class UserProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,

    val isEditDialogVisible: Boolean = false,
    val isPasswordDialogVisible: Boolean = false,
    val isDeleteDialogOpen: Boolean = false,

    val editFields: List<ProfileFieldConfig> = emptyList(),

    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = ""
)

data class ProfileFieldConfig(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val fieldType: ProfileFieldType
)
enum class ProfileFieldType { NAME, SURNAME, USERNAME, PHONE }