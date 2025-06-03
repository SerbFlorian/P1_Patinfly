package cat.deim.asm_22.p1_patinfly.presentation.detailRent

import android.os.Bundle
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
 * Actividad principal de la pantalla de alquiler.
 *
 * Esta actividad es responsable de mostrar la interfaz de usuario para el alquiler de bicicletas,
 * utilizando un diseño en columna que incluye una barra superior de navegación.
 */
class DetailRentBikeActivity : ComponentActivity() {

    /**
     * Identificador de la bicicleta cuyo detalle se va a mostrar.
     */
    private lateinit var bikeId: String

    /**
     * ViewModel asociado para gestionar la lógica y estado de la pantalla.
     */
    private lateinit var viewModel: DetailRentBikeViewModel

    /**
     * Metodo llamado cuando la actividad es creada.
     *
     * Configura el modo Edge-to-Edge para que la interfaz utilice toda la pantalla,
     * obtiene el ID de la bicicleta desde el intent que inició la actividad,
     * inicializa el repositorio y ViewModel para manejar los datos,
     * carga los detalles de la bicicleta y configura el contenido visual con Compose.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el modo Edge-to-Edge para extender la UI hasta los bordes de la pantalla
        enableEdgeToEdge()

        // Obtiene el ID de la bicicleta pasado como argumento en el intent o asigna "Unknown" por defecto
        bikeId = intent.getStringExtra("bikeId") ?: "Unknown"

        // Inicializa las fuentes de datos necesarias para crear el repositorio
        val context = applicationContext
        val bikeDatasource = AppDatabase.getDatabase(context).bikeDataSource()
        val bikeApiDataSource = BikeAPIDataSource.getInstance(context)
        val bikeLocalDataSource = BikeLocalDataSource.getInstance(context)

        // Crea el repositorio combinando las diferentes fuentes de datos
        val repository = BikeRepository(
            bikeDatasource = bikeDatasource,
            apiDataSource = bikeApiDataSource,
            bikeLocalDataSource = bikeLocalDataSource,
            context = context
        )

        // Crea el ViewModel usando una fábrica que recibe el repositorio
        viewModel = ViewModelProvider(
            this,
            DetailRentBikeViewModelFactory(repository)
        )[DetailRentBikeViewModel::class.java]

        // Solicita al ViewModel que cargue los detalles de la bicicleta con el ID recibido
        viewModel.loadBikeDetails(bikeId)

        // Establece la interfaz de usuario con Jetpack Compose
        setContent {
            PatinflyTheme {
                Column {
                    TopAppBar(navigateBack = { finish() })

                    // Observa el estado de la UI desde el ViewModel como estado Compose
                    val uiState = viewModel.uiState.collectAsState()

                    // Pasa el estado actual y el ViewModel a la pantalla de detalle de alquiler
                    DetailRentBikeScreen(uiState = uiState.value, bikeViewModel = viewModel)
                }
            }
        }
    }
}
