package cat.deim.asm_22.p1_patinfly.presentation.detailRent

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Composable principal que muestra la pantalla de detalle y alquiler de una bicicleta.
 *
 * Gestiona el estado de la UI y muestra diferentes vistas según el estado (cargando, error, éxito).
 * También lanza la carga inicial de la primera bicicleta activa.
 *
 * @param uiState Estado actual de la UI que contiene la información sobre la bicicleta y estados de carga.
 * @param bikeViewModel ViewModel asociado a la pantalla para gestionar la lógica y datos.
 */
@SuppressLint("DefaultLocale")
@Composable
fun DetailRentBikeScreen(
    uiState: DetailRentBikeUiState,
    bikeViewModel: DetailRentBikeViewModel = viewModel()
) {
    // Cargar la primera bicicleta activa al iniciar la composición
    LaunchedEffect(Unit) {
        bikeViewModel.loadFirstActiveBike()
    }

    // Manejo de estados de la UI
    when (val state = uiState) {
        is DetailRentBikeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailRentBikeUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is DetailRentBikeUiState.Success -> {
            when {
                state.bike == null -> {
                    // Caso cuando no hay ninguna bicicleta disponible
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "You need to reserve a bike first",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                !state.bike.isActive -> {
                    // Caso cuando la bicicleta existe pero no está activa
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "This bike is not active. Please reserve another one.",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                else -> {
                    // Caso cuando la bicicleta está activa y se muestra el detalle
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = state.bike.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        BikeDetailCard(
                            imageRes = R.drawable.splash_image,
                            bikeType = "EB 0001",
                            isAvailable = state.bike.isActive,
                            modifier = Modifier.fillMaxWidth(),
                            bike = state.bike,
                            onRentClick = { isRented ->
                                val updatedBike = state.bike.copy(isRented = isRented)
                                bikeViewModel.updateBikeRentStatus(updatedBike)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable que representa una tarjeta con el detalle de la bicicleta.
 *
 * Muestra la imagen, estado de disponibilidad, tipo, nivel de batería, distancia y fecha de creación.
 * Permite al usuario iniciar o detener el alquiler de la bicicleta con un botón.
 *
 * @param bike Objeto Bike con los datos de la bicicleta.
 * @param imageRes Recurso de imagen que se mostrará.
 * @param bikeType Tipo o identificador del modelo de bicicleta.
 * @param isAvailable Indica si la bicicleta está disponible para alquiler.
 * @param modifier Modificador para aplicar a la tarjeta.
 * @param onRentClick Callback que se invoca al pulsar el botón de alquiler o paro de alquiler, con el nuevo estado.
 */
@SuppressLint("DefaultLocale")
@Composable
fun BikeDetailCard(
    bike: Bike,
    imageRes: Int,
    bikeType: String,
    isAvailable: Boolean,
    modifier: Modifier,
    onRentClick: (Boolean) -> Unit
) {
    // Formateo de la fecha de creación a formato legible (yyyy-MM-dd)
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = try {
        val dateTime = LocalDateTime.parse(bike.creationDate)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        bike.creationDate
    }

    // Estado para mostrar un posible diálogo de confirmación (no implementado aquí)
    var showConfirmationDialog by remember { mutableStateOf(false) }
    // Estado temporal para mantener el cambio de estado de alquiler pendiente
    var pendingRentStatus by remember { mutableStateOf(bike.isRented) }

    Card(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Bike Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isAvailable) "Available" else "Not Available",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isAvailable) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    Text(
                        text = bikeType,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        onRentClick(pendingRentStatus)
                        pendingRentStatus = !bike.isRented
                        showConfirmationDialog = true
                    },
                    enabled = isAvailable,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (bike.isRented) Color(0xFFF44336) else Color(0xFF5FFF33),
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.DarkGray
                    )
                ) {
                    Text(
                        text = if (bike.isRented) "Stop rent" else "Rent",
                        color = if (bike.isRented) Color.White else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(Icons.Default.BatteryFull, "Battery: ${bike.batteryLevel}%")
                InfoRow(Icons.AutoMirrored.Filled.DirectionsBike, "Distance: ${bike.meters} meters")
                InfoRow(Icons.Default.Person, "Created on: $formattedDate")
            }
        }
    }
}

/**
 * Composable que representa una fila con un icono y texto explicativo.
 *
 * Se usa para mostrar detalles como nivel de batería, distancia o fecha de creación.
 *
 * @param icon Icono a mostrar al inicio de la fila.
 * @param text Texto descriptivo que acompaña al icono.
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
