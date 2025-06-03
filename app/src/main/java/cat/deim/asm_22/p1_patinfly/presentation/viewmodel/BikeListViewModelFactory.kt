package cat.deim.asm_22.p1_patinfly.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.deim.asm_22.p1_patinfly.domain.repository.IBikeRepository

/**
 * Fábrica para crear instancias del [BikeListViewModel].
 * Esta clase se encarga de proporcionar la instancia adecuada del ViewModel,
 * pasando el repositorio necesario al ViewModel.
 *
 * @param repository El repositorio de bicicletas que será proporcionado al ViewModel.
 */
class BikeListViewModelFactory(
    private val repository: IBikeRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    /**
     * Crea una nueva instancia del [BikeListViewModel].
     * Si el [modelClass] es de tipo [BikeListViewModel], se crea una nueva instancia
     * con el repositorio proporcionado.
     * Si el tipo del [modelClass] no coincide, lanza una excepción [IllegalArgumentException].
     *
     * @param modelClass La clase del ViewModel que se quiere crear.
     * @return La instancia del ViewModel solicitada.
     * @throws IllegalArgumentException Si el [modelClass] no es [BikeListViewModel].
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BikeListViewModel::class.java)) {
            return BikeListViewModel(
                repository,
                context = context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}