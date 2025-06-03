package cat.deim.asm_22.p1_patinfly.presentation.detailRent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la lógica y estado para la pantalla
 * de detalles de la bicicleta en alquiler.
 *
 * @property bikeRepository Repositorio para acceder a los datos de bicicletas.
 */
class DetailRentBikeViewModel(
    private val bikeRepository: BikeRepository
) : ViewModel() {

    /**
     * Estado mutable privado que representa la UI actual.
     */
    private val _uiState = MutableStateFlow<DetailRentBikeUiState>(DetailRentBikeUiState.Loading)

    /**
     * Estado inmutable que la UI observa para reflejar cambios.
     */
    val uiState: StateFlow<DetailRentBikeUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de la bicicleta a partir del UUID proporcionado.
     *
     * @param bikeUUID Identificador único de la bicicleta a cargar.
     */
    fun loadBikeDetails(bikeUUID: String) {
        viewModelScope.launch {
            val bike = bikeRepository.getBike(bikeUUID)
            if (bike != null) {
                // Actualiza el estado UI con la bicicleta encontrada.
                _uiState.value = DetailRentBikeUiState.Success(bike)
            } else {
                // Actualiza el estado UI indicando error al no encontrar la bicicleta.
                _uiState.value = DetailRentBikeUiState.Error("Bike not found")
            }
        }
    }

    /**
     * Actualiza el estado de alquiler (rented) de la bicicleta y refleja el cambio en la UI.
     *
     * @param bike Objeto bicicleta cuyo estado de alquiler se quiere cambiar.
     */
    fun updateBikeRentStatus(bike: Bike) {
        viewModelScope.launch {
            try {
                val newStatus = !bike.isRented
                bikeRepository.updateBikeRentStatus(bike.uuid, newStatus)
                // Actualiza el estado UI con el nuevo estado de la bicicleta.
                _uiState.value = DetailRentBikeUiState.Success(bike.copy(isRented = newStatus))
            } catch (e: Exception) {
                // En caso de error, actualiza el estado UI con un mensaje de error.
                _uiState.value =
                    DetailRentBikeUiState.Error("Error updating bike status: ${e.message}")
            }
        }
    }

    /**
     * Carga la primera bicicleta activa disponible y actualiza el estado UI.
     */
    fun loadFirstActiveBike() {
        viewModelScope.launch {
            _uiState.value = DetailRentBikeUiState.Loading

            val activeBike = bikeRepository.getFirstActiveBike()

            // Actualiza el estado UI con la bicicleta activa encontrada (puede ser nulo).
            _uiState.value = DetailRentBikeUiState.Success(activeBike)
        }
    }

}

/**
 * Clase sellada que representa los posibles estados de la UI para la pantalla de detalles de bicicleta.
 */
sealed class DetailRentBikeUiState {
    /**
     * Estado que indica que la información se está cargando.
     */
    data object Loading : DetailRentBikeUiState()

    /**
     * Estado que indica que la información se ha cargado correctamente.
     *
     * @param bike Objeto bicicleta con los datos cargados (puede ser nulo si no hay bicicleta).
     */
    data class Success(val bike: Bike?) : DetailRentBikeUiState()

    /**
     * Estado que indica que ha ocurrido un error durante la carga de datos.
     *
     * @param message Mensaje descriptivo del error ocurrido.
     */
    data class Error(val message: String) : DetailRentBikeUiState()
}
