package cat.deim.asm_22.p1_patinfly.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.data.repository.BikeRepository
import cat.deim.asm_22.p1_patinfly.data.repository.UserRepository
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.domain.models.User
import cat.deim.asm_22.p1_patinfly.presentation.profile.ProfileActivity
import cat.deim.asm_22.p1_patinfly.presentation.detailRent.DetailRentBikeActivity
import cat.deim.asm_22.p1_patinfly.presentation.screen.BikeListActivity
import cat.deim.asm_22.p1_patinfly.presentation.ui.theme.PatinflyTheme
import cat.deim.asm_22.p1_patinfly.presentation.detailBike.DetailBikeActivity
/**
 * MainActivity es la actividad principal de la aplicación, donde se carga la interfaz de usuario
 * y se gestionan las operaciones iniciales como la obtención de datos del usuario y las bicicletas.
 */
class MainActivity : ComponentActivity() {
    /**
     * Metodo que se ejecuta al crear la actividad. Aquí se inicializan los repositorios de usuario y bicicleta,
     * y se establece el contenido de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepository = UserRepository.create(this)
        val bikeRepository = BikeRepository.create(this)

        setContent {
            PatinflyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Estado simple para los datos
                    var user by remember { mutableStateOf<User?>(null) }
                    var bikes by remember { mutableStateOf<List<Bike>>(emptyList()) }
                    var isLoading by remember { mutableStateOf(true) } // Estado de carga

                    // Carga inicial de datos
                    LaunchedEffect(Unit) {
                        user = userRepository.getUser()
                        bikes = bikeRepository.getAll().toList().sortedBy { it.meters }  // Ordenamos las bicicletas por distancia
                        isLoading = false // Marcar como cargado una vez que los datos estén listos
                    }

                    // Solo mostramos los botones y contenido si los datos están cargados
                    if (!isLoading) {
                        // Seleccionamos la primera bicicleta si existe
                        val bike = bikes.firstOrNull()

                        // Pasamos el objeto bike al QRButton
                        if (bike != null) {
                            QRButton(bike = bike)
                        }

                        // Mostramos el contenido principal
                        MainScreenContent(
                            user = user,
                            bikes = bikes
                        )
                    } else {
                        // Puedes agregar un indicador de carga si es necesario
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun MainScreenContent(user: User?, bikes: List<Bike>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppTopBar()

                if (user != null) {
                    MainContent(
                        user = user,
                        bikes = bikes
                    )
                } else {
                    // Muestra un loading simple o contenido vacío
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            val bike = bikes.firstOrNull() // Aquí seleccionamos la primera bicicleta
            if (bike != null) {
                QRButton(bike = bike) // Pasamos el objeto bike
            }
        }
    }
}
/**
 * Composable que crea la barra superior de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar() {
    TopAppBar(
        title = { AppTitle() },
        actions = { MapIcon() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        modifier = Modifier.shadow(4.dp)
    )
}
/**
 * Composable que muestra el título de la aplicación "Patinfly" en la barra superior.
 */
@Composable
private fun AppTitle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 46.dp)
                .weight(1f)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                style = MaterialTheme.typography.displayLarge,
                text = "Patinfly",
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Composable que muestra el icono de mapa en la barra superior.
 */
@Composable
private fun MapIcon() {
    Box(modifier = Modifier.padding(end = 16.dp)) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            color = Color.White
        ) {
            IconButton(
                onClick = { /* Map action */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Filled.Map, "Mapa", tint = Color.Black)
            }
        }
    }
}
/**
 * Composable que muestra el contenido principal de la pantalla, incluyendo el perfil del usuario,
 * bicicletas disponibles, y categorías.
 *
 * @param user El usuario actual.
 * @param bikes La lista de bicicletas disponibles.
 * @param modifier Modificador para ajustar el tamaño y el comportamiento del contenedor.
 */
@Composable
private fun MainContent(user: User?, bikes: List<Bike>) {
    val context = LocalContext.current
    // Callback para actualizar las bicicletas filtradas según la categoría seleccionada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCard(user, context)
        AroundYouSection(context)
        BikeCardsHorizontal(bikes, context)
        CategoriesSection(context = context)
    }
}
/**
 * Composable que muestra la tarjeta de perfil del usuario.
 *
 * @param user El usuario actual.
 * @param context El contexto de la aplicación.
 */
@Composable
private fun ProfileCard(user: User?, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateTo<ProfileActivity>(context, listOf("urban", "electric"))
            },
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage()
            Spacer(modifier = Modifier.width(22.dp))

            if (user != null) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
/**
 * Composable que muestra la imagen del perfil del usuario.
 */
@Composable
private fun ProfileImage() {
    Image(
        painter = painterResource(id = R.drawable.splash_image),
        contentDescription = "Imagen de perfil",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
/**
 * Composable que muestra la sección "Around You", que muestra un botón para ver las bicicletas cercanas.
 *
 * @param context El contexto de la aplicación.
 */
@Composable
private fun AroundYouSection(context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Around you",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .clickable {
                    // Llamamos a navigateTo con el tipo de bicicleta "all"
                    navigateTo<BikeListActivity>(context, listOf("urban", "electric"))

                }
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                // Llamamos a navigateTo con el tipo de bicicleta "all"
                navigateTo<BikeListActivity>(context, listOf("urban", "electric"))
            },
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver todas las bicicletas",
                tint = Color.Black
            )
        }
    }
}

// Función de navegación actualizada para pasar el tipo de bicicleta como parámetro
inline fun <reified T> navigateTo(context: Context, categories: List<String>) {
    val intent = Intent(context, T::class.java).apply {
        putStringArrayListExtra("categories", ArrayList(categories))
    }
    context.startActivity(intent)
}


/**
 * Composable que muestra las tarjetas de bicicletas disponibles en un carrusel horizontal.
 *
 * @param bikes La lista de bicicletas disponibles.
 * @param context El contexto de la aplicación.
 */
@Composable
private fun BikeCardsHorizontal(bikes: List<Bike>, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        bikes.forEach { bike ->
            BikeCard(bike, context)
        }
    }
}
/**
 * Composable que representa una tarjeta individual para una bicicleta, mostrando su imagen y distancia.
 *
 * @param bike La bicicleta que se muestra en la tarjeta.
 * @param context El contexto de la aplicación.
 */
@Composable
private fun BikeCard(bike: Bike, context: Context) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .clickable {

            val intent = Intent(context, DetailBikeActivity::class.java).apply {
                putExtra("bikeId", bike.uuid)
            }
            context.startActivity(intent)

            },
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.splash_image),
                contentDescription = "Imagen de la bicicleta",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${bike.meters} meters away",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

/**
 * Composable que muestra la sección de categorías de bicicletas.
 *
 * @param context El contexto de la aplicación.
 */
@Composable
private fun CategoriesSection(context: Context) {
    val selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(top = 32.dp)) {
        SectionTitle()

        // Condicional para mostrar una categoría o ambas según se haya seleccionado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            // Mostrar "Urban" solo si está seleccionado o no hay selección
            if (selectedCategory == null || selectedCategory == "urban") {
                BikeCategory(
                    icon = Icons.AutoMirrored.Filled.DirectionsBike,
                    label = "Urban",

                    onClick = {
                        val intent = Intent(context, BikeListActivity::class.java)
                        intent.putExtra("category", "urban")
                        context.startActivity(intent)
                    }

                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Mostrar "Electric" solo si está seleccionado o no hay selección
            if (selectedCategory == null || selectedCategory == "electric") {
                BikeCategory(
                    icon = Icons.Default.ElectricBike,
                    label = "Electric",

                    onClick = {
                        val intent = Intent(context, BikeListActivity::class.java)
                        intent.putExtra("category", "electric")
                        context.startActivity(intent)
                    }

                )
            }
        }
    }
}



/**
 * Composable que muestra el título de una sección.
 *
 * @param title El título que se muestra en la sección.
 */
@Composable
private fun SectionTitle() {
    Text(
        text = "Categories",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        textAlign = TextAlign.Start
    )
}

/**
 * Composable que muestra una categoría de bicicleta en una sección de categorías.
 *
 * @param icon El icono que representa el tipo de bicicleta.
 * @param label El nombre de la categoría.
 * @param bikeType El tipo de bicicleta.
 * @param context El contexto de la aplicación.
 */
@Composable
private fun BikeCategory(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable { onClick() },
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 8.dp),
                    tint = Color.Black
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
/**
 * Composable que muestra el botón para escanear un código QR, localizado en la parte inferior derecha de la pantalla.
 *
 * @param modifier Modificador para ajustar el tamaño y el comportamiento del botón.
 */
@Composable
private fun QRButton(bike: Bike) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Surface(
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .clickable {
                    val intent = Intent(context, DetailRentBikeActivity::class.java).apply {
                        putExtra("bikeId", bike.uuid) // Pasamos el ID de la bicicleta
                    }
                    context.startActivity(intent)

                },
            color = Color(0xff5fff33)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "Escanear QR",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Black
                )
            }
        }
    }
}
