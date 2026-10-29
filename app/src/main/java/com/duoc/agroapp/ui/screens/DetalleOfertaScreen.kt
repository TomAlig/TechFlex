package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.DetalleOfertaEstadoUI
import com.duoc.agroapp.ui.components.IconoObstaculo
import com.duoc.agroapp.viewmodel.DetalleOfertaViewModel

//Colores del mockup del Detalle.
private val GrisResumen = Color(0xFFDDE3EA)
private val CremaObstaculo = Color(0xFFFFF1DC)
private val AzulAgua = Color(0xFFE6EDFF)

//Vista del prestador: muestra una solicitud completa y permite enviarle una oferta.
@Composable
fun DetalleOfertaScreen(
    viewModel: DetalleOfertaViewModel,
    solicitudId: Int,
    onVolver: () -> Unit
) {
    val estado = viewModel.estadoUI

    //Carga la solicitud cuando se abre la pantalla.
    LaunchedEffect(solicitudId) {
        viewModel.cargar(solicitudId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        //Encabezado: flecha para volver y título
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Detalle de solicitud", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))

        val solicitud = estado.solicitud
        when {
            estado.cargando -> CircularProgressIndicator(
                color = AzulTechFlex,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            solicitud == null -> Text("No se encontró la solicitud.")
            else -> ContenidoDetalle(estado, solicitud, viewModel)
        }
    }
}

//Todo lo que se muestra cuando la solicitud ya cargó.
@Composable
private fun ContenidoDetalle(
    estado: DetalleOfertaEstadoUI,
    solicitud: SolicitudEntity,
    viewModel: DetalleOfertaViewModel
) {
    //Si la superficie es entera (10.0) se muestra "10".
    val superficie = if (solicitud.superficieHa % 1.0 == 0.0) {
        solicitud.superficieHa.toInt().toString()
    } else {
        solicitud.superficieHa.toString()
    }
    //"Antenas,Cables" -> ["Antenas", "Cables"], igual que en Solicitudes cerca.
    val obstaculos = solicitud.obstaculos.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    Column {
        //Tarjeta resumen: datos principales a la izquierda y estado a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(GrisResumen)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Producto: ${solicitud.tipoProducto}")
                Text("Superficie: $superficie ha")
                Text("Aplicación: ${solicitud.aplicacion}")
            }
            Text(
                text = solicitud.estado,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(50))
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }

        Titulo("Más información")
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(AzulAgua)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.WaterDrop, contentDescription = null, tint = AzulTechFlex, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(if (solicitud.tieneAgua) "Con agua disponible" else "Sin agua disponible", fontSize = 13.sp)
        }

        Titulo("Obstáculos (${obstaculos.size} reportados)")
        if (obstaculos.isEmpty()) {
            Text("Sin obstáculos reportados", fontSize = 13.sp)
        }
        //Una fila por obstáculo, con el mismo ícono naranjo de Solicitudes cerca
        obstaculos.forEach { obstaculo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(CremaObstaculo)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconoObstaculo(obstaculo)
                Spacer(Modifier.width(8.dp))
                Text(obstaculo, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Titulo("Comentarios adicionales")
        Text(solicitud.comentarios.ifBlank { "Sin comentarios" })

        Spacer(Modifier.height(20.dp))

        if (estado.ofertaEnviada) {
            //Si ya ofertó, en vez del formulario se muestra un aviso.
            Text(
                "¡Oferta enviada! El productor la verá en sus ofertas.",
                color = AzulTechFlex,
                fontWeight = FontWeight.Bold
            )
        } else {
            val errorPrecio = estado.errorPrecio
            OutlinedTextField(
                value = estado.precioTexto,
                onValueChange = { viewModel.onPrecioChange(it) },
                label = { Text("Ingresar precio por ha") },
                prefix = { Text("$ ") },
                singleLine = true,
                isError = errorPrecio != null,
                supportingText = if (errorPrecio != null) { { Text(errorPrecio) } } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),   //teclado numérico
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulTechFlex,
                    focusedLabelColor = AzulTechFlex,
                    cursorColor = AzulTechFlex
                )
            )

            //El total se recalcula solo mientras el prestador escribe (ver precioTotal en el estado).
            val total = estado.precioTotal
            Text(
                text = if (total != null) {
                    "El precio total de la aplicación será de " + formatearPesos(total) + " + IVA"
                } else {
                    "Ingresa un precio para ver el total de la aplicación"
                },
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(16.dp))

            if (estado.enviando) {
                CircularProgressIndicator(color = AzulTechFlex, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = { viewModel.onOfertarClick() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
                ) {
                    Text("¡Ofertar!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            //Ej: "Ya enviaste una oferta para esta solicitud"
            estado.errorGeneral?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

//Título de cada sección, con el mismo estilo en todas.
@Composable
private fun Titulo(texto: String) {
    Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 14.dp, bottom = 6.dp))
}

//1000 -> "$ 1.000" (formato chileno con punto de miles).
private fun formatearPesos(monto: Long): String {
    return "$ " + "%,d".format(monto).replace(',', '.')
}