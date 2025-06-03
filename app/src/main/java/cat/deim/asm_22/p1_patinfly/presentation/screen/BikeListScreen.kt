package cat.deim.asm_22.p1_patinfly.presentation.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.deim.asm_22.p1_patinfly.R
import cat.deim.asm_22.p1_patinfly.domain.models.Bike
import cat.deim.asm_22.p1_patinfly.presentation.detailBike.DetailBikeActivity

/**
 * Composable que muestra el contenido de la lista de bicicletas disponibles.
 * Dependiendo de si hay bicicletas disponibles o no, muestra un mensaje de "No hay bicicletas disponibles"
 * o una lista de bicicletas con tarjetas que contienen detalles.
 *
 * @param bikes Colección de objetos [Bike] que se mostrarán en la lista.
 */
@Composable
fun BikeListScreen(bikes: Collection<Bike>) {
    // Si no hay bicicletas disponibles, muestra un mensaje
    when {
        bikes.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay bicicletas disponibles",
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        else -> {
            // Si hay bicicletas, muestra una lista ordenada por la distancia (más cercana primero)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Ordena las bicicletas por distancia en metros (ascendente)
                items(bikes.sortedBy { it.meters }) { bike ->
                    BikeCard(bike = bike)
                }
            }
        }
    }
}

/**
 * Composable que muestra una tarjeta con la información de una bicicleta.
 * Incluye una imagen, la distancia, el estado de la batería y el tipo de bicicleta,
 * además de un botón para ver los detalles de la bicicleta.
 *
 * @param bike Objeto de tipo [Bike] que contiene la información que se muestra en la tarjeta.
 */
@SuppressLint("DefaultLocale")
@Composable
fun BikeCard(bike: Bike) {
    val context = LocalContext.current

    // Crea una tarjeta que contiene la información de la bicicleta
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            // Imagen de la bicicleta
            Image(
                painter = painterResource(id = R.drawable.splash_image),
                contentDescription = "Imagen de la bicicleta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(21.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Distancia de la bicicleta en kilómetros formateada con un decimal
            val distanceKm = String.format("%.1f", bike.meters / 1000.0)
            Text(
                text = "$distanceKm km away",
                style = MaterialTheme.typography.bodyMedium,
                color =  Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    // Estado de la batería en texto ("High", "Medium", "Low")
                    val batteryStatus = when {
                        bike.batteryLevel > 50 -> "High"
                        bike.batteryLevel == 50 -> "Medium"
                        else -> "Low"
                    }
                    Text(text = batteryStatus, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "battery",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            // Tipo de bicicleta mostrado en texto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = bike.bikeType.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Botón que abre la actividad de detalle de la bicicleta, pasando su ID
            Button(
                onClick = {
                    val intent = Intent(context, DetailBikeActivity::class.java).apply {
                        putExtra("bikeId", bike.uuid) // Pasamos el ID de la bicicleta
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff5fff33))
            ) {
                Text(
                    text = "View Details",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
