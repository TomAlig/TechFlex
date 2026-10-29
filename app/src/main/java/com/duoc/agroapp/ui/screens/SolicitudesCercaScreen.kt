package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.R
import com.duoc.agroapp.ui.components.SolicitudCercanaCard
import com.duoc.agroapp.viewmodel.SolicitudesCercaViewModel

//Vista del prestador: lista de solicitudes abiertas, de la más cercana a la más lejana.
@Composable
fun SolicitudesCercaScreen(
    viewModel: SolicitudesCercaViewModel,
    onVerDetalle: (Int) -> Unit,        //recibe el id de la solicitud tocada
    onPerfilClick: () -> Unit = {}      //se conecta cuando exista la vista de perfil
) {
    val estado = viewModel.estadoUI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        //Encabezado: logo de Tecmundo Connect a la izquierda, avatar a la derecha
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo_tecmundo_connect),
                contentDescription = "Logo Tecmundo Connect",
                //Se fija solo la altura; el ancho se ajusta solo para no deformar la imagen.
                modifier = Modifier.height(60.dp),
                contentScale = ContentScale.Fit
            )
            //Empuja el avatar hacia la derecha.
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Mi perfil",
                tint = Color.Black,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable { onPerfilClick() }
            )
        }
        Spacer(Modifier.height(12.dp))

        Text("Solicitudes cerca de ti", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        //Tres casos: cargando, sin solicitudes o la lista
        when {
            estado.cargando -> {
                CircularProgressIndicator(
                    color = AzulTechFlex,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            estado.solicitudes.isEmpty() -> {
                Text("No hay solicitudes cerca por ahora.", style = MaterialTheme.typography.bodyMedium)
            }
            else -> {
                //LazyColumn solo dibuja las tarjetas visibles en pantalla, así sirve para listas largas.
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    //itemsIndexed entrega también la posición, para numerar las tarjetas (1, 2, 3...).
                    itemsIndexed(estado.solicitudes, key = { _, solicitud -> solicitud.id }) { indice, solicitud ->
                        SolicitudCercanaCard(
                            numero = indice + 1,
                            solicitud = solicitud,
                            onVerDetalle = { onVerDetalle(solicitud.id) }
                        )
                    }
                }
            }
        }
    }
}