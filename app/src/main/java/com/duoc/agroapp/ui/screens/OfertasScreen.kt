package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duoc.agroapp.R
import com.duoc.agroapp.ui.components.OfertaCard
import com.duoc.agroapp.viewmodel.OfertasViewModel

@Composable
fun OfertasScreen(
    viewModel: OfertasViewModel,
    onVolver: () -> Unit
) {
    val estado = viewModel.estadoUI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp
            )
    ) {

        // Fila superior con el logo a la izquierda y el usuario a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = com.duoc.agroapp.R.drawable.logo_tecmundo_connect),
                contentDescription = "Logo Tecmundo",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.placeholder_user),
                contentDescription = "Perfil de usuario",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(estado.ofertas, key = { oferta -> oferta.id }) { oferta ->
                OfertaCard(
                    oferta = oferta,
                    esFavorita = estado.favoritosIds.contains(oferta.id),
                    aceptada = estado.ofertaAceptadaId == oferta.id,
                    puedeAceptar = estado.ofertaAceptadaId == null,
                    onFavorito = { viewModel.onFavoritoClick(oferta.id) },
                    onAceptar = { viewModel.onAceptarClick(oferta) },
                    onContactar = { viewModel.onContactarClick(oferta) }
                )
            }
        }
    }

    // Modal de confirmación para aceptar
    estado.ofertaPorAceptar?.let { oferta ->
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarAceptar() },
            title = { Text("Aceptar oferta") },
            text = { Text("¿Quieres aceptar la oferta de ${oferta.nombrePrestador}? Las demás ofertas quedarán bloqueadas.") },
            confirmButton = { TextButton(onClick = { viewModel.onConfirmarAceptar() }) { Text("Aceptar") } },
            dismissButton = { TextButton(onClick = { viewModel.onCancelarAceptar() }) { Text("Cancelar") } }
        )
    }

    // Modal de contacto (simulado)
    estado.ofertaContactada?.let { oferta ->
        AlertDialog(
            onDismissRequest = { viewModel.onCerrarContacto() },
            title = { Text("Contactar") },
            text = { Text("Se envió tu mensaje a ${oferta.nombrePrestador}. Te responderá a la brevedad.") },
            confirmButton = { TextButton(onClick = { viewModel.onCerrarContacto() }) { Text("Entendido") } }
        )
    }
}