package cat.deim.asm_22.p1_patinfly.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.deim.asm_22.p1_patinfly.domain.models.Credentials
import cat.deim.asm_22.p1_patinfly.domain.usecase.LoginUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de login que gestiona:
 * - El estado de la UI (email, contraseña, validaciones)
 * - La lógica de autenticación
 * - La comunicación con el caso de uso de login
 *
 * @property loginUseCase Caso de uso que contiene la lógica de negocio para autenticación
 */
class LoginViewModel(private val loginUseCase: LoginUsecase) : ViewModel() {
    // Estado interno mutable
    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estado público observable de la UI
     * Contiene:
     * - Email y contraseña actuales
     * - Estados de validación
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el estado del email y valida su existencia si no está vacío
     * @param email Nuevo valor del campo email
     */
    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
        if (email.isNotEmpty()) {
            validateUser(email)
        }
    }

    /**
     * Actualiza el estado de la contraseña y valida las credenciales si no está vacía
     * @param password Nuevo valor del campo contraseña
     */
    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
        if (password.isNotEmpty()) {
            validatePassword()
        }
    }

    /**
     * Valida si el usuario existe en el sistema
     * @param email Email a validar
     */
    private fun validateUser(email: String) {
        viewModelScope.launch {
            val userExists = loginUseCase.checkUserExists(email)
            _uiState.value = _uiState.value.copy(isUserValid = userExists)
        }
    }

    /**
     * Valida si las credenciales actuales (email + contraseña) son correctas
     */
    private fun validatePassword() {
        viewModelScope.launch {
            val credentials = _uiState.value.run { Credentials(email, password) }
            val isValid = loginUseCase.execute(credentials)
            _uiState.value = _uiState.value.copy(isPasswordValid = isValid)
        }
    }

    /**
     * Determina si el login está permitido en el estado actual
     * @return true si tanto el email como la contraseña son válidos
     */
    fun login() = _uiState.value.isUserValid && _uiState.value.isPasswordValid
}

/**
 * Representa el estado de la UI para la pantalla de login
 * @property email Email introducido por el usuario
 * @property password Contraseña introducida por el usuario
 * @property isUserValid Indica si el email existe en el sistema
 * @property isPasswordValid Indica si la contraseña coincide con el email
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isUserValid: Boolean = false,
    val isPasswordValid: Boolean = false
)