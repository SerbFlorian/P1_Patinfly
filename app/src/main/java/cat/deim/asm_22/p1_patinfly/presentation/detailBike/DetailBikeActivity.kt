package cat.deim.asm_22.p1_patinfly.presentation.detailBike

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.local.BikeLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.BikeAPIDataSource
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import cat.deim.asm_22.p1_patinfly.presentation.components.TopAppBar
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme

/**
 * Actividad que muestra el detalle de una bicicleta.
 *
 * Se encarga de inicializar el ViewModel, cargar los detalles de la bicicleta
 * y renderizar la UI correspondiente según el estado de carga, éxito o error.
 */
class DetailBikeActivity : ComponentActivity() {

    /**
     * Identificador de la bicicleta cuyo detalle se mostrará.
     */
    private lateinit var bikeId: String

    /**
     * ViewModel asociado para manejar la lógica y estado de la vista.
     */
    private lateinit var viewModel: DetailBikeViewModel

    /**
     * Metodo invocado al crear la actividad.
     * Inicializa las dependencias, obtiene el ID de la bicicleta y configura la UI.
     *
     * @param savedInstanceState Bundle con estado previo de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtener el ID de la bicicleta desde el Intent; si no existe, se asigna "Unknown"
        bikeId = intent.getStringExtra("bikeId") ?: "Unknown"

        // Inicializar dependencias necesarias para el repositorio
        val context = applicationContext
        val bikeDatasource = AppDatabase.getDatabase(context).bikeDataSource()
        val bikeApiDataSource = BikeAPIDataSource.getInstance(context)
        val bikeLocalDataSource = BikeLocalDataSource.getInstance(context)

        // Crear el repositorio con las fuentes de datos
        val repository = BikeRepository(
            bikeDatasource = bikeDatasource,
            apiDataSource = bikeApiDataSource,
            bikeLocalDataSource = bikeLocalDataSource,
            context = context
        )

        // Crear el ViewModel usando un ViewModelFactory personalizado
        viewModel = ViewModelProvider(
            this,
            DetailBikeViewModelFactory(repository)
        )[DetailBikeViewModel::class.java]

        // Cargar los detalles de la bicicleta con el ID obtenido
        viewModel.loadBikeDetails(bikeId)

        // Configurar el contenido de la UI usando Jetpack Compose
        setContent {
            PatinflyTheme {
                Column {
                    // Barra superior con botón para regresar
                    TopAppBar(navigateBack = { finish() })

                    // Obtener el estado actual del UI desde el ViewModel
                    val uiState = viewModel.uiState.collectAsState()

                    // Renderizar la UI según el estado actual
                    when (val state = uiState.value) {
                        is DetailBikeUiState.Loading -> {
                            // Aquí se podría mostrar un indicador de carga
                        }
                        is DetailBikeUiState.Success -> {
                            // Mostrar los detalles de la bicicleta cuando se cargan correctamente
                            DetailBikeScreen(
                                bike = state.bike,
                                viewModel = viewModel
                            )
                        }
                        is DetailBikeUiState.Error -> {
                            // Registrar el error en el log si falla la carga de datos
                            Log.e("DetailBikeActivity", "Error: ${state.message}")
                        }
                    }
                }
            }
        }
    }
}
