package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.agroapp.ui.components.SolicitudCard
import com.duoc.agroapp.viewmodel.MisSolicitudesViewModel

@Composable
fun MisSolicitudesScreen(
    viewModel: MisSolicitudesViewModel,
    onNuevaSolicitud: () -> Unit,
    onVerOfertas: (Int) -> Unit
) {
    val estado = viewModel.estadoUI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text("Mis solicitudes", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onNuevaSolicitud,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Nueva solicitud")
        }
        Spacer(Modifier.height(12.dp))

        when {
            estado.cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            estado.solicitudes.isEmpty() -> {
                Text(
                    "Aún no tienes solicitudes. Crea la primera con el botón de arriba.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    //La key permite que al eliminar una tarjeta, las demás se muevan con animación.
                    items(estado.solicitudes, key = { solicitud -> solicitud.id }) { solicitud ->
                        SolicitudCard(
                            solicitud = solicitud,
                            onEliminar = { viewModel.onEliminarClick(solicitud) },
                            onVerOfertas = { onVerOfertas(solicitud.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    // Modal de confirmación para eliminar
    estado.solicitudAEliminar?.let { solicitud ->
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarEliminar() },
            title = { Text("Eliminar solicitud") },
            text = { Text("¿Seguro que quieres eliminar la solicitud de ${solicitud.superficieHa} ha? Esta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = { viewModel.onConfirmarEliminar() }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { viewModel.onCancelarEliminar() }) { Text("Cancelar") } }
        )
    }
}