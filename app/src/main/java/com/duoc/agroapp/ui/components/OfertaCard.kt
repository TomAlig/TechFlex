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
import com.duoc.agroapp.model.OFERTA_ACEPTADA
import com.duoc.agroapp.model.OFERTA_PENDIENTE
import com.duoc.agroapp.model.OFERTA_RECHAZADA
import com.duoc.agroapp.model.OfertaConPrestador
import com.duoc.agroapp.ui.screens.AzulTechFlex
import java.text.NumberFormat
import java.util.Locale

private val VerdeAceptada = Color(0xFF1B6B3A)

//Tarjeta de una oferta: datos del prestador, detalle desplegable y botones Aceptar / Contactar.
@Composable
fun OfertaCard(
    oferta: OfertaConPrestador,
    esFavorita: Boolean,
    onFavorito: () -> Unit,
    onAceptar: () -> Unit,
    onContactar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datos = oferta.oferta
    val aceptada = datos.estado == OFERTA_ACEPTADA

    //Estado solo visual: si el detalle está desplegado.
    var detalleVisible by remember { mutableStateOf(false) }

    //Al aceptarse la oferta, el fondo cambia a verde con una transición suave.
    val colorFondo by animateColorAsState(
        targetValue = if (aceptada) Color(0xFFD9F2E0) else MaterialTheme.colorScheme.surfaceVariant,
        label = "colorOferta"
    )

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
                    Text(
                        "Precio por ha: $ ${formatoPesos(datos.precioPorHa)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.clickable { detalleVisible = !detalleVisible },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Detalle", style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            imageVector = if (detalleVisible) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (detalleVisible) "Ocultar detalle" else "Ver detalle"
                        )
                    }
                }
                IconButton(onClick = onFavorito) {
                    Icon(
                        imageVector = if (esFavorita) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = if (esFavorita) "Quitar de favoritos" else "Marcar como favorito"
                    )
                }
            }

            //Animación: el detalle se despliega y se oculta con una transición suave.
            AnimatedVisibility(visible = detalleVisible) {
                Text(
                    "Total estimado: $ ${formatoPesos(datos.precioTotal)} + IVA",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAceptar,
                    //Solo se puede aceptar una oferta que sigue pendiente.
                    enabled = datos.estado == OFERTA_PENDIENTE,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulTechFlex,
                        disabledContainerColor = if (aceptada) VerdeAceptada else AzulTechFlex.copy(alpha = 0.4f),
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(
                        when (datos.estado) {
                            OFERTA_ACEPTADA -> "Aceptada"
                            OFERTA_RECHAZADA -> "Rechazada"
                            else -> "Aceptar"
                        }
                    )
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

//Da formato de pesos chilenos con punto de miles (ej: 10000 -> 10.000).
private fun formatoPesos(monto: Long): String {
    return NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(monto)
}