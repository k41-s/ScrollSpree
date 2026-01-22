package com.k41s.scrollspree.ui.screens.user.mainTabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.dto.ChangePasswordDTO
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.UserDTO
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val email = tokenManager.email.first()

            if (email == null) {
                _state.update {
                    it.copy(isLoading = false, errorMessage = "User session not found")
                }
                return@launch
            }

            when (val result = userRepository.getByEmail(email)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, user = result.data) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> { _state.update { it.copy(isLoading = false) } }
            }
        }
    }

    private fun updateFieldInList(type: ProfileFieldType, newValue: String) {
        _state.update { currentState ->
            val updatedList = currentState.editFields.map { field ->
                if (field.fieldType == type) field.copy(value = newValue) else field
            }
            currentState.copy(editFields = updatedList)
        }
    }

    fun toggleEditDialog(show: Boolean) {
        if (show) {
            val user = _state.value.user
            _state.update { it ->
                it.copy(
                isEditDialogVisible = true,
                editFields = listOf(
                    ProfileFieldConfig(
                        "First Name",
                        user?.firstName ?: "",
                        {
                            updateFieldInList(ProfileFieldType.NAME, it)
                        },
                        ProfileFieldType.NAME
                    ),
                    ProfileFieldConfig(
                        "Last Name",
                        user?.lastName ?: "",
                        {
                            updateFieldInList(ProfileFieldType.SURNAME, it)
                        },
                        ProfileFieldType.SURNAME
                    ),
                    ProfileFieldConfig(
                        "Username",
                        user?.username ?: "",
                        {
                            updateFieldInList(ProfileFieldType.USERNAME, it)
                        },
                        ProfileFieldType.USERNAME
                    ),
                    ProfileFieldConfig(
                        "Phone Number",
                        user?.phone ?: "",
                        {
                            updateFieldInList(ProfileFieldType.PHONE, it)
                        },
                        ProfileFieldType.PHONE
                    )
                )
            ) }
        } else {
            _state.update { it.copy(isEditDialogVisible = false) }
        }
    }

    fun onSaveProfileClicked() {
        val currentState = _state.value
        val currentUser = currentState.user ?: return
        val userId = currentUser.id

        if (userId == null) {
            _state.update { it.copy(errorMessage = "Critical Error: User ID not found.") }
            return
        }

        val newUsername = currentState.editFields.find {
            it.fieldType == ProfileFieldType.USERNAME
        }?.value ?: currentUser.username

        val name = currentState.editFields.find {
            it.fieldType == ProfileFieldType.NAME
        }?.value ?: ""

        val surname = currentState.editFields.find {
            it.fieldType == ProfileFieldType.SURNAME
        }?.value ?: ""

        val phone = currentState.editFields.find {
            it.fieldType == ProfileFieldType.PHONE
        }?.value ?: ""


        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val dto = UserDTO(
                id = userId,
                email = currentUser.email,
                username = newUsername,
                name = name,
                surname = surname,
                phone = phone,
                role = currentUser.role
            )

            when (val result = userRepository.updateProfileByEmail(currentUser.email, dto)) {
                is NetworkResult.Success -> {
                    val currentPassword = tokenManager.password.first() ?: ""

                    val loginResult = authRepository.login(
                        LoginDTO(newUsername, currentPassword)
                    )

                    if (loginResult is NetworkResult.Success) {
                        _events.send("Profile updated successfully!")
                        _state.update { it.copy(isEditDialogVisible = false, isLoading = false) }

                        loadProfile()
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Profile updated, but session refresh failed. Please re-login."
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onOldPasswordChanged(v: String) =
        _state.update { it.copy(oldPassword = v, errorMessage = null) }

    fun onNewPasswordChanged(v: String) =
        _state.update { it.copy(newPassword = v, errorMessage = null) }

    fun onConfirmPasswordChanged(v: String) =
        _state.update { it.copy(confirmPassword = v, errorMessage = null) }

    fun togglePasswordDialog(show: Boolean) {
        _state.update {
            it.copy(
                isPasswordDialogVisible = show,
                oldPassword = "",
                newPassword = "",
                confirmPassword = "",
                errorMessage = null
            )
        }
    }

    fun onChangePasswordClicked() {
        val uiState = _state.value
        val username = uiState.user?.username ?: return

        if (uiState.oldPassword.isBlank() || uiState.newPassword.isBlank()) {
            _state.update { it.copy(errorMessage = "Fields cannot be empty") }
            return
        }

        if (uiState.newPassword != uiState.confirmPassword) {
            _state.update { it.copy(errorMessage = "New passwords do not match") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val dto = ChangePasswordDTO(
                username = username,
                oldPassword = uiState.oldPassword,
                newPassword = uiState.newPassword
            )

            when (val result = userRepository.changePassword(dto)) {
                is NetworkResult.Success -> {
                    tokenManager.savePassword(uiState.newPassword)

                    _events.send("Password updated successfully!")
                    togglePasswordDialog(false)
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }
}