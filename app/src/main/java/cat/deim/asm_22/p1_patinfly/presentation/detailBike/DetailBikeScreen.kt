package cat.deim.asm_22.p1_patinfly.presentation.detailBike

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Composable que muestra la pantalla de detalle de una bicicleta.
 *
 * Permite reservar o liberar la bicicleta, actualizando el estado y comunicándolo
 * al ViewModel.
 *
 * @param bike Objeto Bike con la información de la bicicleta a mostrar.
 * @param viewModel ViewModel asociado para manejar la lógica de negocio.
 */
@Composable
@SuppressLint("DefaultLocale")
fun DetailBikeScreen(bike: Bike, viewModel: DetailBikeViewModel) {
    // Estado que indica si la bicicleta está reservada (true) o libre (false)
    var isReserved by remember { mutableStateOf(bike.isActive) }

    val coroutineScope = rememberCoroutineScope()

    val buttonText = if (isReserved) "Reserved" else "Reserve now"
    val buttonColor = if (isReserved) Color.LightGray else Color(0xFF5FFF33)

    Column(modifier = Modifier.padding(16.dp)) {
        BikeDetailCard(
            imageRes = R.drawable.splash_image,
            bikeType = "EB 0001",
            isAvailable = !isReserved,
            modifier = Modifier.fillMaxWidth(),
            bike = bike,
            buttonText = buttonText,
            buttonColor = buttonColor,
            onReserveClick = {
                coroutineScope.launch {
                    // Invoca al ViewModel para alternar el estado de reserva
                    viewModel.toggleReservation(bike)
                    // Actualiza el estado local para reflejar la reserva
                    isReserved = true
                }
            }
        )
    }
}

/**
 * Composable que representa una tarjeta con los detalles de una bicicleta.
 *
 * Muestra la imagen, estado de disponibilidad, tipo, fecha de creación y otros datos
 * relevantes, así como un botón para reservar o liberar la bicicleta.
 *
 * @param bike Objeto Bike con la información de la bicicleta.
 * @param imageRes Recurso drawable para la imagen de la bicicleta.
 * @param bikeType Cadena que identifica el tipo o código de la bicicleta.
 * @param buttonText Texto que aparecerá en el botón de reserva.
 * @param buttonColor Color de fondo del botón.
 * @param isAvailable Indica si la bicicleta está disponible para reservar.
 * @param modifier Modificador para configurar el layout.
 * @param onReserveClick Lambda que se ejecuta al pulsar el botón de reserva.
 */
@Composable
fun BikeDetailCard(
    bike: Bike,
    imageRes: Int,
    bikeType: String,
    buttonText: String,
    buttonColor: Color,
    isAvailable: Boolean,
    modifier: Modifier,
    onReserveClick: () -> Unit
) {
    // Formateador para mostrar la fecha de creación en formato "yyyy-MM-dd"
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Intenta parsear la fecha de creación y formatearla; si falla, muestra la original
    val formattedDate = try {
        val dateTime = LocalDateTime.parse(bike.creationDate)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        bike.creationDate
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            // Fila superior con imagen, disponibilidad, tipo y botón de reserva
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
                    onClick = onReserveClick,
                    enabled = isAvailable,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.DarkGray,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = buttonText)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // Fila inferior con iconos e información adicional
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(
                    icon = Icons.Default.BatteryFull,
                    text = "Battery: ${bike.batteryLevel}%"
                )
                InfoRow(
                    icon = Icons.AutoMirrored.Filled.DirectionsBike,
                    text = "Distance: ${bike.meters} meters"
                )
                InfoRow(
                    icon = Icons.Default.Person,
                    text = "Created on: $formattedDate"
                )
            }
        }
    }
}

/**
 * Composable que muestra una fila con un icono y un texto asociado.
 *
 * @param icon Icono a mostrar.
 * @param text Texto que acompaña al icono.
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
