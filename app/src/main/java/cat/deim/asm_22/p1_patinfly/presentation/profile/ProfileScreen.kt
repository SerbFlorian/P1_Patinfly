package cat.deim.asm_22.p1_patinfly.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.data.datasource.database.model.BikeDTO
import cat.deim.asm_22.p1_patinfly.data.datasource.local.model.UserModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/**
 * Composable principal que muestra la pantalla de perfil.
 * Muestra un estado basado en la UI (cargando, éxito, error).
 *
 * @param viewModel ViewModel que gestiona el estado y datos del perfil.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> LoadingProfile()
            is ProfileUiState.Success -> {
                UserProfileCard(user = state.user)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Rental History",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
                RentalHistorySection(
                    bikes = state.user?.rentalHistory ?: emptyList(),
                    viewModel = viewModel
                )
            }
            is ProfileUiState.Error -> ErrorProfile(message = state.message)
        }
    }
}

/**
 * Sección que muestra el historial de alquileres.
 * Si no hay historial, muestra un texto indicando ausencia.
 *
 * @param bikes Lista de bicicletas alquiladas.
 * @param viewModel ViewModel para manejar acciones de bicicletas.
 */
@Composable
fun RentalHistorySection(
    bikes: List<BikeDTO>,
    viewModel: ProfileViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (bikes.isEmpty()) {
            Text(
                text = "No rental history available.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            bikes.forEach { bike ->
                BikeCard(
                    bike = bike,
                    viewModel = viewModel,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * Composable que muestra una tarjeta con los detalles de una bicicleta.
 * Permite expandir para ver más detalles y eliminar la reserva.
 *
 * @param bike Datos de la bicicleta.
 * @param viewModel ViewModel para manejar acciones sobre la bicicleta.
 * @param modifier Modificador opcional para la tarjeta.
 */
@Composable
fun BikeCard(
    bike: BikeDTO,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = try {
        val dateTime = LocalDateTime.parse(bike.creationDate)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        bike.creationDate
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bike.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoRow(
                        Icons.Default.BatteryFull,
                        "Battery: ${bike.batteryLevel}%"
                    )
                    InfoRow(
                        Icons.AutoMirrored.Filled.DirectionsBike,
                        "Distance: ${bike.meters} meters"
                    )
                    InfoRow(
                        Icons.Default.Person,
                        formattedDate
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val updatedBike = bike.copy(isActive = false)
                            viewModel.updateBikeStatus(updatedBike)
                            viewModel.removeBikeFromHistory(bike.uuid)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Text(
                            text = "Delete reservation",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fila que muestra un icono y un texto asociado, usado para detalles.
 *
 * @param icon Icono a mostrar.
 * @param text Texto descriptivo asociado al icono.
 */
@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Tarjeta que muestra la información básica del usuario.
 *
 * @param user Modelo con los datos del usuario.
 */
@Composable
private fun UserProfileCard(user: UserModel?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImageSection()
            Spacer(modifier = Modifier.width(16.dp))
            UserDetailsSection(user = user)
        }
    }
}

/**
 * Sección que muestra la imagen de perfil con un texto debajo.
 */
@Composable
private fun ProfileImageSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Profile",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
    }
}

/**
 * Sección que muestra los detalles del usuario: nombre, email, fechas de creación y última conexión.
 * Formatea las fechas si es posible, o muestra valores por defecto en caso de error.
 *
 * @param user Modelo con los datos del usuario.
 */
@Composable
private fun UserDetailsSection(user: UserModel?) {
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val formattedCreationDate = try {
        val dateTime = LocalDateTime.parse(user?.creationDate)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        user?.creationDate?.substringBefore('T') ?: "N/A"
    }

    val formattedLastConnection = try {
        val dateTime = LocalDateTime.parse(user?.lastConnection)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        user?.lastConnection?.substringBefore('T') ?: "N/A"
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (user != null) {
            ProfileDetailItem(label = "Name", value = user.name, isHeader = true)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (user != null) {
            ProfileDetailItem(label = "Email", value = user.email)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (user != null) {
            ProfileDetailItem(label = "Date of creation", value = formattedCreationDate)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (user != null) {
            ProfileDetailItem(label = "Last connection", value = formattedLastConnection)
        }
    }
}

/**
 * Elemento que muestra una línea con etiqueta y valor, aplicando estilos para encabezado o detalle.
 *
 * @param label Texto que representa el nombre del campo.
 * @param value Texto que representa el valor del campo.
 * @param isHeader Indica si el texto debe mostrarse con estilo de encabezado.
 */
@Composable
private fun ProfileDetailItem(label: String, value: String, isHeader: Boolean = false) {
    Text(
        text = "$label: $value",
        style = if (isHeader) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.bodyLarge
        },
        fontSize = if (isHeader) 18.sp else 16.sp
    )
}

/**
 * Composable que muestra un indicador de carga centrado en pantalla.
 */
@Composable
private fun LoadingProfile() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Composable que muestra un mensaje de error centrado en pantalla con estilo de error.
 *
 * @param message Texto con el mensaje de error a mostrar.
 */
@Composable
private fun ErrorProfile(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
