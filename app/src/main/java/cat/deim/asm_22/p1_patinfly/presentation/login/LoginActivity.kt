package cat.deim.asm_22.p1_patinfly.presentation.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cat.deim.asm_22.p1_patinfly.data.datasource.database.AppDatabase
import cat.deim.asm_22.p1_patinfly.data.repository.UserRepository
import cat.deim.asm_22.p1_patinfly.domain.usecase.LoginUsecase
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme

/**
 * Actividad principal para el proceso de autenticación de usuarios.
 *
 * Esta actividad configura:
 * - La interfaz de usuario de login usando Jetpack Compose
 * - Las dependencias necesarias (DataSource, Repository, UseCase)
 * - El ViewModel con la lógica de autenticación
 *
 * El flujo incluye:
 * 1. Configuración inicial de la vista con Edge-to-Edge
 * 2. Aplicación del tema visual Patinfly
 * 3. Inyección de dependencias
 * 4. Visualización del formulario de login
 */
class LoginActivity : ComponentActivity() {

    /**
     * Metodo llamado al crear la actividad. Configura:
     * - La vista principal con diseño Edge-to-Edge
     * - El tema de la aplicación
     * - Las dependencias del sistema de autenticación
     * - La pantalla de login con su ViewModel
     *
     * @param savedInstanceState Estado previo de la actividad (nullable)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño Edge-to-Edge (contenido bajo barras del sistema)
        enableEdgeToEdge()

        setContent {
            // Aplica el tema visual personalizado
            PatinflyTheme {
                // Surface base que ocupa toda la pantalla
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Obtiene el contexto actual para inyección de dependencias
                    val context = LocalContext.current

                    // Configuración del repositorio usando la base de datos Room
                    val userDataSource = AppDatabase.getDatabase(context).userDataSource()
                    val userRepository = UserRepository(userDataSource, context)

                    // Configuración del caso de uso de autenticación
                    val loginUseCase = LoginUsecase(userRepository)

                    // Muestra la pantalla de login con el ViewModel configurado
                    LoginScreen(
                        viewModel = LoginViewModel(loginUseCase)
                    )
                }
            }
        }
    }
}