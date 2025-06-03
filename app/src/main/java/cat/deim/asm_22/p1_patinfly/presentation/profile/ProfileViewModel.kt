package cat.deim.asm_22.p1_patinfly.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO
import cat.deim.asm_22.patinfly.data.datasource.local.UserLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.UserModel
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado de la pantalla de perfil del usuario.
 * Este ViewModel obtiene los datos del perfil de usuario desde el repositorio de datos locales.
 *
 * @param userDataSource El origen de datos local para obtener la información del usuario.
 */
class ProfileViewModel(
    private val userDataSource: UserLocalDataSource,
    private val bikeRepository: BikeRepository // Agregar BikeRepository como dependencia
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = userDataSource.getUser()
            if (user != null) {
                val activeBikes = bikeRepository.getActiveBikes() // Obtenemos las bicicletas activas
                _uiState.value =
                    ProfileUiState.Success(user.copy(rentalHistory = activeBikes)) // Actualizamos el estado con bicicletas activas
            } else {
                _uiState.value = ProfileUiState.Error("User not found")
            }
        }
    }
    fun updateBikeStatus(bike: BikeDTO) {
        viewModelScope.launch {
            try {
                val newStatus = bike.isActive // Deactivamos la bicicleta
                bikeRepository.updateBikeStatus(bike.uuid, newStatus)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error updating bike status: ${e.message}")
            }
        }
    }
    // En tu ProfileViewModel
    fun removeBikeFromHistory(bikeId: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is ProfileUiState.Success -> {
                    val updatedUser = currentState.user?.copy(
                        rentalHistory = currentState.user.rentalHistory.filter { it.uuid != bikeId }
                    )
                    currentState.copy(user = updatedUser)
                }
                else -> currentState
            }
        }
    }

}


/**
 * Clase sellada que representa los diferentes estados de la UI para el perfil de usuario.
 */
sealed class ProfileUiState {

    /**
     * Estado que indica que se está cargando el perfil del usuario.
     */
    data object Loading : ProfileUiState()

    /**
     * Estado que indica que el perfil del usuario se ha cargado correctamente.
     *
     * @param user El objeto de usuario con los detalles cargados.
     */
    data class Success(val user: UserModel?) : ProfileUiState()

    /**
     * Estado que indica que ocurrió un error al cargar el perfil del usuario.
     *
     * @param message El mensaje de error.
     */
    data class Error(val message: String) : ProfileUiState()
}