package cat.deim.asm_22.p1_patinfly.presentation.detailRent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository

/**
 * Factoría para crear instancias de DetailRentBikeViewModel
 * proporcionando el repositorio necesario.
 *
 * @property bikeRepository Repositorio de bicicletas que se inyecta en el ViewModel.
 */
class DetailRentBikeViewModelFactory(private val bikeRepository: BikeRepository) : ViewModelProvider.Factory {

    /**
     * Crea una instancia del ViewModel solicitado.
     *
     * @param modelClass Clase del ViewModel que se desea crear.
     * @return Instancia del ViewModel solicitado.
     * @throws IllegalArgumentException si la clase no corresponde a DetailRentBikeViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailRentBikeViewModel::class.java)) {
            return DetailRentBikeViewModel(bikeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
