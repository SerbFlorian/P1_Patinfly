package cat.deim.asm_22.p1_patinfly.presentation.detailBike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository

/**
 * Fábrica para crear instancias de DetailBikeViewModel.
 *
 * @property bikeRepository Repositorio necesario para el ViewModel.
 */
class DetailBikeViewModelFactory(private val bikeRepository: BikeRepository) : ViewModelProvider.Factory {

    /**
     * Crea una instancia del ViewModel solicitado.
     *
     * @param modelClass Clase del ViewModel a crear.
     * @return Instancia del ViewModel correspondiente.
     * @throws IllegalArgumentException si la clase no es DetailBikeViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailBikeViewModel::class.java)) {
            return DetailBikeViewModel(bikeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
