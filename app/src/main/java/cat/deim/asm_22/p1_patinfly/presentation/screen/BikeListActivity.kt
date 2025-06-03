package cat.deim.asm_22.p1_patinfly.presentation.screen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.datasource.local.BikeLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.BikeAPIDataSource
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme
import cat.deim.asm_22.p1_patinfly.presentation.components.TopAppBar
import cat.deim.asm_22.p1_patinfly.presentation.viewmodel.BikeListViewModel
import cat.deim.asm_22.p1_patinfly.presentation.viewmodel.BikeListViewModelFactory

/**
 * Actividad que muestra la lista de bicicletas disponibles en la aplicación.
 * Se encarga de inicializar las dependencias necesarias y configurar la vista Compose.
 */
class BikeListActivity : ComponentActivity() {

    /**
     * Metodo llamado al crear la actividad.
     * Inicializa la UI con Compose, configura el ViewModel y obtiene las bicicletas filtradas según la categoría.
     * @param savedInstanceState Bundle con el estado previo de la actividad (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PatinflyTheme {
                val context = LocalContext.current

                // Inicialización de las fuentes de datos y repositorio para obtener bicicletas
                val dataSource = AppDatabase.getDatabase(context).bikeDataSource()
                val apiDataSource = BikeAPIDataSource.getInstance(context)
                val localDataSource = BikeLocalDataSource.getInstance(context)
                val repository = BikeRepository(
                    dataSource,
                    apiDataSource,
                    localDataSource,
                    context
                )

                // Creación del ViewModel con la factoría correspondiente
                val viewModel: BikeListViewModel = viewModel(
                    factory = BikeListViewModelFactory(
                        repository = repository,
                        context = context
                    )
                )

                // Observación de la lista de bicicletas desde el ViewModel
                val bikes by viewModel.bikes.collectAsState()

                // Obtención de las categorías pasadas por el intent
                val categories = intent.getStringArrayListExtra("categories")
                val category = intent.getStringExtra("category")
                Log.d("BikeListActivity", "Categories recibidas: $categories, category: $category")

                // Filtrado de bicicletas por categoría
                // Si hay varias categorías, se obtiene la lista combinada usando flatMap
                // Si solo hay una categoría diferente de "all", se filtra por esa categoría
                // Si no se especifica ninguna, se muestran todas las bicicletas
                val filteredBikes = categories?.flatMap { category ->
                    val bikesByCategory = repository.getBikesByCategory(category)
                    bikesByCategory.onEach { bike ->
                        Log.d("FILTER", "Bike ${bike.name} type: ${bike.bikeType.name}")
                    }
                }
                    ?: if (category != null && category != "all") {
                        repository.getBikesByCategory(category)
                    } else {
                        bikes // Mostrar todas si no se pasa categoría
                    }

                // Construcción de la interfaz de usuario con la lista de bicicletas filtradas
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar(navigateBack = { finish() })
                    BikeListScreen(bikes = filteredBikes)
                }
            }
        }
    }
}
