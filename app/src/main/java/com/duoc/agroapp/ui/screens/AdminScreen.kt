package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.viewmodel.AdminViewModel

//Vista del administrador: lista de usuarios registrados con opción de eliminarlos.
@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val estado = viewModel.estadoUI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text("Administración", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Usuarios registrados: ${estado.usuarios.size}", color = Color.Gray)
        Spacer(Modifier.height(12.dp))

        when {
            estado.cargando -> CircularProgressIndicator(
                color = AzulTechFlex,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            estado.usuarios.isEmpty() -> Text("Todavía no hay usuarios registrados.")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(estado.usuarios, key = { it.email }) { usuario ->
                    TarjetaUsuario(usuario, onEliminar = { viewModel.onEliminarClick(usuario) })
                }
            }
        }
    }

    //Modal de confirmación antes de eliminar (mismo patrón que Mis solicitudes).
    estado.usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarEliminar() },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Seguro que quieres eliminar a ${usuario.nombre} (${usuario.email})? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmarEliminar() }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onCancelarEliminar() }) { Text("Cancelar") }
            }
        )
    }
}

//Tarjeta de un usuario: nombre, correo, tipo y botón de eliminar.
@Composable
private fun TarjetaUsuario(usuario: UserEntity, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(usuario.nombre.ifBlank { "Sin nombre" }, fontWeight = FontWeight.Bold)
                Text(usuario.email, fontSize = 13.sp, color = Color.Gray)
                Text(usuario.tipoUsuario, fontSize = 13.sp, color = AzulTechFlex, fontWeight = FontWeight.SemiBold)
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar usuario", tint = Color.Red)
            }
        }
    }
}