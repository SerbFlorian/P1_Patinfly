package cat.deim.asm_22.p1_patinfly.presentation.detailBike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la lógica de negocio y estado UI
 * para la pantalla de detalle de una bicicleta.
 *
 * @property bikeRepository Repositorio para obtener y actualizar datos de bicicletas.
 */
class DetailBikeViewModel(
    private val bikeRepository: BikeRepository
) : ViewModel() {

    /**
     * Estado mutable interno que representa el estado actual de la UI.
     */
    private val _uiState = MutableStateFlow<DetailBikeUiState>(DetailBikeUiState.Loading)

    /**
     * Estado público inmutable expuesto para la UI.
     */
    val uiState: StateFlow<DetailBikeUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de una bicicleta dado su UUID.
     *
     * @param bikeUUID Identificador único de la bicicleta a cargar.
     * Se actualiza el estado de la UI con el resultado:
     * - Success si la bicicleta fue encontrada.
     * - Error si hubo un fallo o no se encontró la bicicleta.
     */
    fun loadBikeDetails(bikeUUID: String) {
        viewModelScope.launch {
            try {
                val bike = bikeRepository.getBike(bikeUUID)
                if (bike != null) {
                    _uiState.value = DetailBikeUiState.Success(bike)
                } else {
                    _uiState.value = DetailBikeUiState.Error("Bike not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailBikeUiState.Error("Error loading bike details: ${e.message}")
            }
        }
    }

    /**
     * Cambia el estado de reserva de la bicicleta (activa o no).
     *
     * @param bike Objeto Bike que se desea reservar o liberar.
     * Se actualiza el estado de la UI con la bicicleta modificada o un error si falla la actualización.
     */
    fun toggleReservation(bike: Bike) {
        viewModelScope.launch {
            try {
                val newStatus = !bike.isActive
                bikeRepository.updateBikeStatus(bike.uuid, newStatus)
                // Actualiza el estado UI con la nueva bicicleta
                _uiState.value = DetailBikeUiState.Success(bike.copy(isActive = newStatus))
            } catch (e: Exception) {
                _uiState.value = DetailBikeUiState.Error("Error updating bike status: ${e.message}")
            }
        }
    }
}

/**
 * Representa los diferentes estados posibles de la UI para la pantalla de detalle de bicicleta.
 */
sealed class DetailBikeUiState {

    /**
     * Estado que indica que los datos están cargando.
     */
    data object Loading : DetailBikeUiState()

    /**
     * Estado que indica que la carga fue exitosa.
     *
     * @property bike La bicicleta cargada y lista para mostrar.
     */
    data class Success(val bike: Bike) : DetailBikeUiState()

    /**
     * Estado que indica que ocurrió un error durante la carga o actualización.
     *
     * @property message Mensaje descriptivo del error.
     */
    data class Error(val message: String) : DetailBikeUiState()
}
