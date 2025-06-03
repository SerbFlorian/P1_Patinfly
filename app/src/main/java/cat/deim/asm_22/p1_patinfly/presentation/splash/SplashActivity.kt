package cat.deim.asm_22.p1_patinfly.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.presentation.login.LoginActivity
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme
/**
 * SplashActivity es la actividad inicial que se muestra al usuario
 * cuando abre la aplicación. Se encarga de mostrar una pantalla
 * de bienvenida antes de redirigir al usuario a la actividad de login.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PatinflyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashContent {
                        // Redirige a LoginActivity cuando se presiona el botón
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // Cierra la SplashActivity para evitar que el usuario regrese a ella
                    }
                }
            }
        }
    }
}

/**
 * Composable que representa el contenido visual de la SplashScreen.
 * Muestra el texto de bienvenida, un subtítulo, una imagen y un botón
 * para iniciar la aplicación redirigiendo al usuario a la pantalla de login.
 *
 * @param onButtonClick Lambda que se ejecuta cuando el botón es presionado,
 * redirigiendo a la siguiente pantalla de la aplicación.
 */
@Composable
fun SplashContent(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp
        )
        // Texto grande de "Patinfly"
        Text(
            text = "Patinfly",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp
        )
        // Subtexto "Explore urban areas on two wheels"
        Text(
            text = "Explore urban areas on two wheels",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), // Color con transparencia
            modifier = Modifier.padding(top = 30.dp,bottom = 30.dp) // Espacio debajo del subtexto
        )
        // Imagen (asegúrate de tener una imagen en res/drawable/splash.png)
        Image(
            painter = painterResource(id = R.drawable.splash_image), // Sin la extensión .png
            contentDescription = "Splash Image",
            modifier = Modifier
                .size(400.dp) // Tamaño de la imagen
                .padding(bottom = 16.dp)
        )

        // Botón para redirigir a LoginActivity
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .width(400.dp)
                .padding(top = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff5fff33)) // Color del botón
        ) {
            Text(
                text= "Get Started",
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}