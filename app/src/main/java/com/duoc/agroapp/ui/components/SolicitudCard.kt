package com.duoc.agroapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.ESTADO_CON_OFERTAS
import com.duoc.agroapp.model.ESTADO_FINALIZADA
import com.duoc.agroapp.model.ESTADO_PENDIENTE
import com.duoc.agroapp.ui.screens.AzulTechFlex
import androidx.compose.runtime.getValue

//Tarjeta de una solicitud: foto, datos principales, estado y botón de eliminar (solo si está pendiente).
@Composable
fun SolicitudCard(
    solicitud: SolicitudEntity,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Si la superficie es entera (10.0) se muestra "10", si tiene decimales se deja igual.
    val superficieTexto = if (solicitud.superficieHa % 1.0 == 0.0) {
        solicitud.superficieHa.toInt().toString()
    } else {
        solicitud.superficieHa.toString()
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoSolicitud(solicitud.fotoUri)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Producto: ${solicitud.tipoProducto}", style = MaterialTheme.typography.bodyMedium)
                Text("Superficie: $superficieTexto ha", style = MaterialTheme.typography.bodyMedium)
                Text("Aplicación: ${solicitud.aplicacion}", style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                ChipEstado(solicitud.estado)
                if (solicitud.estado == ESTADO_PENDIENTE) {
                    IconButton(onClick = onEliminar) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar solicitud")
                    }
                }
            }
        }
    }
}

//Muestra la foto de la solicitud o un ícono si todavía no tiene foto.
@Composable
private fun FotoSolicitud(fotoUri: String?) {
    val formaFoto = RoundedCornerShape(8.dp)
    if (fotoUri != null) {
        AsyncImage(
            model = fotoUri,
            contentDescription = "Foto del terreno",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .clip(formaFoto)
        )
    } else {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(formaFoto)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Agriculture, contentDescription = "Sin foto")
        }
    }
}

//Etiqueta de color según el estado. Al cambiar de estado el color se anima.
@Composable
private fun ChipEstado(estado: String) {
    val (colorFondo, colorTexto) = when (estado) {
        ESTADO_CON_OFERTAS -> Color(0xFFDCE6FF) to AzulTechFlex
        ESTADO_FINALIZADA -> Color(0xFFD9F2E0) to Color(0xFF1B6B3A)
        else -> Color(0xFFFFF3CD) to Color(0xFF8A6D00)
    }
    val fondoAnimado by animateColorAsState(targetValue = colorFondo, label = "colorEstado")

    Text(
        text = estado,
        color = colorTexto,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(fondoAnimado)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}