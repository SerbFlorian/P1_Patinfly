package cat.deim.asm_22.p1_patinfly.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.ServerStatus
import cat.deim.asm_22.p1_patinfly.domain.repository.IBikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class BikeListViewModel(
    private val repository: IBikeRepository,
    private val context: Context
) : ViewModel() {

    // Estado para las bicicletas
    private val _bikes = MutableStateFlow<List<Bike>>(emptyList())
    val bikes: StateFlow<List<Bike>> = _bikes.asStateFlow()

    // Estado para el status del servidor
    private val _serverStatus = MutableStateFlow<ServerStatus?>(null)

    // Estado de carga/error
    private val _isLoading = MutableStateFlow(false)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtener estado del servidor desde LiveData
                val liveData = ServerStatus.execute(context)
                liveData.observeForever { result ->
                    result.onSuccess { status ->
                        _serverStatus.value = status
                    }.onFailure { e ->
                        Log.e("BikeListVM", "Error getting server status", e)
                    }
                }

                // 2. Obtener bicicletas
                _bikes.value = repository.getAll().toList()
            } catch (e: Exception) {
                Log.e("BikeListVM", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

}