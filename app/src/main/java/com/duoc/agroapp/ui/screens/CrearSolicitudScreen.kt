package com.duoc.agroapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.duoc.agroapp.model.OBSTACULOS_DISPONIBLES
import com.duoc.agroapp.model.OBSTACULO_OTROS
import com.duoc.agroapp.model.OPCIONES_APLICACION
import com.duoc.agroapp.model.TIPOS_PRODUCTO
import com.duoc.agroapp.viewmodel.CrearSolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearSolicitudScreen(
    viewModel: CrearSolicitudViewModel,
    onSolicitudCreada: () -> Unit,
    onVolver: () -> Unit
) {
    val estado = viewModel.estadoUI
    var menuExpandido by remember { mutableStateOf(false) }

    //Cuando se guarda la solicitud, se navega a la pantalla siguiente.
    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            viewModel.onNavegacionConsumida()
            onSolicitudCreada()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Crear Solicitud", style = MaterialTheme.typography.headlineMedium)
        }

        // Tipo de producto (desplegable)
        ExposedDropdownMenuBox(expanded = menuExpandido, onExpandedChange = { menuExpandido = it }) {
            OutlinedTextField(
                value = estado.tipoProducto,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de producto") },
                placeholder = { Text("Seleccionar…") },
                isError = estado.errorTipoProducto != null,
                supportingText = { estado.errorTipoProducto?.let { Text(it) } },
                trailingIcon = {
                    if (estado.errorTipoProducto != null) IconoError()
                    else ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido)
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = menuExpandido, onDismissRequest = { menuExpandido = false }) {
                TIPOS_PRODUCTO.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            viewModel.onTipoProductoChange(tipo)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        // Superficie
        OutlinedTextField(
            value = estado.superficie,
            onValueChange = { viewModel.onSuperficieChange(it) },
            label = { Text("Superficie (ha)") },
            placeholder = { Text("Ej: 10") },
            isError = estado.errorSuperficie != null,
            supportingText = { estado.errorSuperficie?.let { Text(it) } },
            trailingIcon = { if (estado.errorSuperficie != null) IconoError() },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ¿Tiene agua?
        Text("¿Tiene agua?", style = MaterialTheme.typography.titleMedium)
        Row {
            OpcionRadio("Sí", estado.tieneAgua) { viewModel.onTieneAguaChange(true) }
            OpcionRadio("No", !estado.tieneAgua) { viewModel.onTieneAguaChange(false) }
        }

        // Aplicación
        Text("Aplicación", style = MaterialTheme.typography.titleMedium)
        Row {
            OPCIONES_APLICACION.forEach { opcion ->
                OpcionRadio(opcion, estado.aplicacion == opcion) { viewModel.onAplicacionChange(opcion) }
            }
        }

        // Obstáculos
        Text("Obstáculos", style = MaterialTheme.typography.titleMedium)
        OBSTACULOS_DISPONIBLES.forEach { obstaculo ->
            val marcado = estado.obstaculos.contains(obstaculo)
            Row(
                modifier = Modifier.toggleable(
                    value = marcado,
                    role = Role.Checkbox,
                    onValueChange = { viewModel.onObstaculoChange(obstaculo, it) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = marcado, onCheckedChange = null)
                Text(obstaculo, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Animación: el aviso aparece con fade/expand solo al marcar "Otros".
        AnimatedVisibility(visible = estado.obstaculos.contains(OBSTACULO_OTROS)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = AzulTechFlex)
                Text(
                    "Describe el obstáculo en los comentarios",
                    color = AzulTechFlex,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Comentarios
        OutlinedTextField(
            value = estado.comentarios,
            onValueChange = { viewModel.onComentariosChange(it) },
            label = { Text("Comentarios adicionales") },
            placeholder = { Text("Ej: acceso por camino de tierra") },
            isError = estado.errorComentarios != null,
            supportingText = { Text(estado.errorComentarios ?: "${estado.comentarios.length}/200") },
            trailingIcon = { if (estado.errorComentarios != null) IconoError() },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        if (estado.guardando) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = { viewModel.onEnviarClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
            ) {
                Text("Crear solicitud")
            }
        }
    }

    // Modal de confirmación
    if (estado.mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarConfirmacion() },
            title = { Text("Confirmar solicitud") },
            text = { Text("¿Deseas crear la solicitud de ${estado.superficie} ha con producto ${estado.tipoProducto.lowercase()}?") },
            confirmButton = { TextButton(onClick = { viewModel.onConfirmarEnvio() }) { Text("Confirmar") } },
            dismissButton = { TextButton(onClick = { viewModel.onCancelarConfirmacion() }) { Text("Cancelar") } }
        )
    }
}

//Ícono rojo que se muestra al final de un campo con error.
@Composable
private fun IconoError() {
    Icon(Icons.Filled.Error, contentDescription = "Campo con error", tint = MaterialTheme.colorScheme.error)
}

//Opción de un grupo de radio buttons con texto.
@Composable
private fun OpcionRadio(texto: String, seleccionada: Boolean, alHacerClick: () -> Unit) {
    Row(
        modifier = Modifier
            .selectable(selected = seleccionada, role = Role.RadioButton, onClick = alHacerClick)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = seleccionada, onClick = null)
        Text(texto, modifier = Modifier.padding(start = 8.dp))
    }
}