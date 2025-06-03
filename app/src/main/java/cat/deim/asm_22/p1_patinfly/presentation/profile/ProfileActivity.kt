package cat.deim.asm_22.p1_patinfly.presentation.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.patinfly.data.datasource.local.UserLocalDataSource
import cat.deim.asm_22.p1_patinfly.data.datasource.local.BikeLocalDataSource // Asegúrate de tener este repositorio
import cat.deim.asm_22.p1_patinfly.data.datasource.remote.BikeAPIDataSource
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository // Asegúrate de tener este repositorio
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme
import cat.deim.asm_22.p1_patinfly.presentation.components.TopAppBar


/**
 * Actividad que muestra el perfil del usuario.
 *
 * Esta actividad configura:
 * - La interfaz de usuario del perfil
 * - La barra de aplicación superior personalizada
 * - Las dependencias necesarias para obtener los datos del usuario
 *
 * Flujo principal:
 * 1. Configura el diseño edge-to-edge
 * 2. Aplica el tema de la aplicación
 * 3. Muestra la barra superior y la pantalla de perfil
 */
class ProfileActivity : ComponentActivity() {

    /**
     * Metodo llamado al crear la actividad. Realiza:
     * - Configuración inicial del diseño edge-to-edge
     * - Inicialización de dependencias (UserLocalDataSource y BikeLocalDataSource)
     * - Configuración del contenido con Compose
     *
     * @param savedInstanceState Estado previo de la actividad (puede ser nulo)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño edge-to-edge (extiende contenido bajo las barras del sistema)
        enableEdgeToEdge()

        // Obtener dependencias
        val context = applicationContext
        val userDataSource = UserLocalDataSource.getInstance(context)
        val bikeDatasource = AppDatabase.getDatabase(context).bikeDataSource()
        val bikeApiDataSource = BikeAPIDataSource.getInstance(context)
        val bikeLocalDataSource = BikeLocalDataSource.getInstance(context)

        // Crear el repositorio de bicicletas
        val repository = BikeRepository(
            bikeDatasource = bikeDatasource,
            apiDataSource = bikeApiDataSource,
            bikeLocalDataSource = bikeLocalDataSource,
            context = context
        )

        setContent {
            PatinflyTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar(navigateBack = { finish() })

                    // La pantalla de perfil necesita un ViewModel que obtenga la información del usuario
                    ProfileScreen(
                        viewModel = ProfileViewModel(
                            userDataSource = userDataSource,
                            bikeRepository = repository // Pasamos bikeRepository aquí
                        )
                    )
                }
            }
        }
    }
}
