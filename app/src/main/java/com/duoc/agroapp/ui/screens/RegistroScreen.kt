package com.duoc.agroapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.R
import com.duoc.agroapp.model.TIPO_PRESTADOR
import com.duoc.agroapp.model.TIPO_PRODUCTOR
import com.duoc.agroapp.viewmodel.RegistroViewModel

//Vista de registro (global): crea la cuenta de un prestador o productor.
@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroExitoso: () -> Unit      //la navegación decide a dónde ir (ej: volver al login)
) {
    val estado = viewModel.estadoUI

    //Cuando el registro termina bien, se navega y se limpia el formulario.
    //LaunchedEffect solo se ejecuta cuando cambia registroExitoso, no en cada redibujo.
    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) {
            onRegistroExitoso()
            viewModel.onNavegacionConsumida()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())   //permite bajar si el teclado tapa los campos
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tecmundo_connect),
            contentDescription = "Logo Tecmundo Connect",
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.Fit
        )

        CampoRegistro("Rut", estado.rut, viewModel::onRutChange, estado.errorRut)
        CampoRegistro("Nombre", estado.nombre, viewModel::onNombreChange, estado.errorNombre)
        CampoRegistro(
            "Correo", estado.correo, viewModel::onCorreoChange, estado.errorCorreo,
            tipoTeclado = KeyboardType.Email
        )
        CampoRegistro(
            "Contraseña", estado.contrasena, viewModel::onContrasenaChange, estado.errorContrasena,
            tipoTeclado = KeyboardType.Password, esContrasena = true
        )

        //Mockup: "se despliega para repetir". Aparece cuando el usuario empieza a escribir la contraseña.
        AnimatedVisibility(visible = estado.contrasena.isNotEmpty()) {
            CampoRegistro(
                "Repetir contraseña", estado.repetirContrasena, viewModel::onRepetirContrasenaChange, estado.errorRepetir,
                tipoTeclado = KeyboardType.Password, esContrasena = true
            )
        }

        CampoRegistro(
            "Teléfono", estado.telefono, viewModel::onTelefonoChange, estado.errorTelefono,
            tipoTeclado = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Fila inferior del mockup: suscripción a la izquierda, tipo de usuario a la derecha
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Suscribirse a novedades", fontSize = 13.sp)
                OpcionRadio("Sí", estado.suscritoNovedades == true) { viewModel.onSuscritoChange(true) }
                OpcionRadio("No", estado.suscritoNovedades == false) { viewModel.onSuscritoChange(false) }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonTipoUsuario(TIPO_PRESTADOR, estado.tipoUsuario == TIPO_PRESTADOR) {
                    viewModel.onTipoUsuarioChange(TIPO_PRESTADOR)
                }
                BotonTipoUsuario(TIPO_PRODUCTOR, estado.tipoUsuario == TIPO_PRODUCTOR) {
                    viewModel.onTipoUsuarioChange(TIPO_PRODUCTOR)
                }
            }
        }
        estado.errorTipoUsuario?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(20.dp))

        //Mientras se guarda se muestra una ruedita; así no se puede tocar Registrarse dos veces.
        if (estado.guardando) {
            CircularProgressIndicator(color = AzulTechFlex)
        } else {
            Button(
                onClick = { viewModel.onRegistrarClick() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        //Correo o RUT repetido (viene del repositorio)
        estado.errorGeneral?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "© 2026 TecMundo. \nTodos los derechos reservados.", color = Color.DarkGray)
    }
}

//Campo de texto reutilizable, para no repetir el mismo OutlinedTextField seis veces.
@Composable
private fun CampoRegistro(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    error: String?,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,     //pinta el borde rojo si hay error
        //El texto de error se muestra debajo solo si existe; si no, no ocupa espacio.
        supportingText = if (error != null) { { Text(error) } } else null,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),   //teclado de números, correo, etc.
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AzulTechFlex,
            focusedLabelColor = AzulTechFlex,
            cursorColor = AzulTechFlex
        )
    )
}

//Opción Sí/No. selectable hace que se pueda tocar todo (el círculo y el texto).
@Composable
private fun OpcionRadio(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.selectable(selected = seleccionado, onClick = onClick, role = Role.RadioButton)
    ) {
        RadioButton(
            selected = seleccionado,
            onClick = null,   //el click lo maneja la fila completa
            colors = RadioButtonDefaults.colors(selectedColor = AzulTechFlex)
        )
        Text(texto)
    }
}

//Botón de tipo de usuario: relleno si está elegido, solo con borde si no.
@Composable
private fun BotonTipoUsuario(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    if (seleccionado) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
        ) { Text(texto) }
    } else {
        OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Text(texto, color = AzulTechFlex)
        }
    }
}