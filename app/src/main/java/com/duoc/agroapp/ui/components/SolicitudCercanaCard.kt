package com.duoc.agroapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.model.SolicitudCercana
import com.duoc.agroapp.ui.screens.AzulTechFlex

private val GrisTarjeta = Color(0xFFF1F4F8)
private val GrisEtiqueta = Color(0xFFE3E7EE)
private val AzulClaro = Color(0xFFE6EDFF)
private val NaranjaObstaculo = Color(0xFFE07A00)

//Tarjeta de una solicitud cercana: número, producto, distancia, superficie, aplicación y obstáculos.
@Composable
fun SolicitudCercanaCard(
    numero: Int,
    solicitud: SolicitudCercana,
    onVerDetalle: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Si la superficie es entera (10.0) se muestra "10".
    val superficieTexto = if (solicitud.superficieHa % 1.0 == 0.0) {
        solicitud.superficieHa.toInt().toString()
    } else {
        solicitud.superficieHa.toString()
    }

    Card(
        onClick = onVerDetalle,   //toda la tarjeta abre el detalle
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = GrisTarjeta)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            //Fila 1: número, producto y distancia
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(26.dp).clip(CircleShape).background(AzulTechFlex),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$numero", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = solicitud.tipoProducto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)   //ocupa el espacio libre y empuja la distancia a la derecha
                )
                Etiqueta("A ${solicitud.distanciaKm} km", fondo = AzulClaro, colorTexto = AzulTechFlex)
            }
            Spacer(Modifier.height(6.dp))

            //Fila 2: superficie y aplicación, alineadas con el nombre del producto
            Row(
                modifier = Modifier.padding(start = 34.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Etiqueta("$superficieTexto ha", fondo = GrisEtiqueta)
                Etiqueta("Aplicación ${solicitud.aplicacion.lowercase()}", fondo = GrisEtiqueta)
            }
            Spacer(Modifier.height(8.dp))

            //Fila 3: íconos de obstáculos y "Ver detalle"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    solicitud.obstaculos.forEach { IconoObstaculo(it) }
                }
                Text("Ver detalle ›", color = AzulTechFlex, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

//Etiqueta redondeada pequeña ("A 12 km", "10 ha").
@Composable
private fun Etiqueta(texto: String, fondo: Color, colorTexto: Color = Color(0xFF374151)) {
    Text(
        text = texto,
        color = colorTexto,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(fondo)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

//Círculo naranjo con el ícono de cada obstáculo, como en el mockup.
//Los textos son los mismos de OBSTACULOS_DISPONIBLES (Crear solicitud de Luis).
@Composable
fun IconoObstaculo(obstaculo: String) {
    val icono = when (obstaculo) {
        "Antenas" -> Icons.Filled.CellTower
        "Cables" -> Icons.Filled.Cable
        "Torres de alta tensión" -> Icons.Filled.Bolt
        else -> Icons.Filled.MoreHoriz   //"Otros" o cualquier obstáculo nuevo
    }
    Box(
        modifier = Modifier.size(24.dp).clip(CircleShape).background(NaranjaObstaculo),
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = obstaculo, tint = Color.White, modifier = Modifier.size(14.dp))
    }
}

//Vista previa con datos de ejemplo, para ver la tarjeta sin correr la app.
@Preview(showBackground = true)
@Composable
private fun SolicitudCercanaCardPreview() {
    SolicitudCercanaCard(
        numero = 1,
        solicitud = SolicitudCercana(1, "Granulada", 10.0, "Diurna", listOf("Cables", "Torres de alta tensión", "Antenas"), 12),
        onVerDetalle = {}
    )
}