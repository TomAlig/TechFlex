package com.duoc.agroapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.agroapp.model.OfertaEjemplo
import com.duoc.agroapp.ui.screens.AzulTechFlex
import java.util.Locale

private val VerdeAceptada = Color(0xFF1B6B3A)

//Tarjeta de una oferta: datos del prestador, descripción desplegable y botones Aceptar / Contactar.
@Composable
fun OfertaCard(
    oferta: OfertaEjemplo,
    esFavorita: Boolean,
    aceptada: Boolean,
    puedeAceptar: Boolean,
    onFavorito: () -> Unit,
    onAceptar: () -> Unit,
    onContactar: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Estado solo visual: si la descripción está desplegada.
    var descripcionVisible by remember { mutableStateOf(false) }

    //Al aceptarse la oferta, el fondo cambia a verde con una transición suave.
    val colorFondo by animateColorAsState(
        targetValue = if (aceptada) Color(0xFFD9F2E0) else MaterialTheme.colorScheme.surfaceVariant,
        label = "colorOferta"
    )
    //La calificación se muestra con coma decimal, como en el diseño (3,4).
    val calificacionTexto = "%.1f".format(Locale.forLanguageTag("es-CL"), oferta.calificacion)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FotoPrestador()
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        oferta.nombrePrestador,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Precio por ha: ${oferta.precioPorHa}", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.clickable { descripcionVisible = !descripcionVisible },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Descripción", style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            imageVector = if (descripcionVisible) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (descripcionVisible) "Ocultar descripción" else "Ver descripción"
                        )
                    }
                }
                Text(calificacionTexto, style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onFavorito) {
                    Icon(
                        imageVector = if (esFavorita) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = if (esFavorita) "Quitar de favoritos" else "Marcar como favorito"
                    )
                }
            }

            //Animación: la descripción se despliega y se oculta con una transición suave.
            AnimatedVisibility(visible = descripcionVisible) {
                Text(oferta.descripcion, style = MaterialTheme.typography.bodySmall)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAceptar,
                    enabled = puedeAceptar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulTechFlex,
                        disabledContainerColor = if (aceptada) VerdeAceptada else AzulTechFlex.copy(alpha = 0.4f),
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(if (aceptada) "Aceptada" else "Aceptar")
                }
                Button(
                    onClick = onContactar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
                ) {
                    Text("Contactar")
                }
            }
        }
    }
}

//Círculo con la foto del prestador (por ahora un ícono; se puede cambiar por una imagen).
@Composable
private fun FotoPrestador() {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, AzulTechFlex, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.Person, contentDescription = "Foto del prestador", tint = AzulTechFlex)
    }
}