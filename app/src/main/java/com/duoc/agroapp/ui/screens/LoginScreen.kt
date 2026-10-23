package com.duoc.agroapp.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.viewmodel.LoginViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import com.duoc.agroapp.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.*
val AzulTechFlex = Color(0xFF3668FF)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // con el valor de estado se va actualizando por cada input.
    val estado = viewModel.estadoUI

    //Checkbox button para recordar al usuario. (Falta darle funcion.)
    var recuerdame by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }

    //Variables usadas para el modal de recuperar contraseña.
    var ModalRecuperar by remember {mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-70).dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tecmundo_connect), //Foto de drawable del logo (png)
            contentDescription = "Logo Tecmundo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "BIENVENID@!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(5.dp))

        // Campo de texto para Correo
        OutlinedTextField(
            value = estado.email,
            // Cada vez que el usuario escribe, VM actualiza el texto.
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para Contraseña
        OutlinedTextField(
            value = estado.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), //Coloca puntos visibles en lugar de texto.
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulTechFlex,
                focusedLabelColor = AzulTechFlex,
                cursorColor = AzulTechFlex
            )

        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { recuerdame = !recuerdame }, // Hace que al tocar el texto también se active
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox para recordar usuario
            Checkbox(
                checked = true,
                enabled = false,
                onCheckedChange = { recuerdame = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = AzulTechFlex // Se pinta del azul al estar checkeado.
                )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Recuérdame (Siempre activo en esta version.)",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Si el ViewModel define el estado como cargando, muestra un circulo de carga.
        if (estado.cargando) {
            CircularProgressIndicator()
        } else {
            // Si no está cargando, muestra el botón normal
            Button(
                onClick = { viewModel.iniciarSesion() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulTechFlex
                )
            ) {
                Text("Iniciar Sesión")
            }
        }

        // Si hay un resultado de login (éxito o error), lo muestra abajo
        estado.loginResultado?.let { resultado ->
            Spacer(modifier = Modifier.height(16.dp))
            if (resultado.exito) {
                Text("Login correcto.", color = Color.Green)
            } else {
                Text(resultado.errorMsg ?: "Error", color = Color.Red)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //Recuperar Contraseña
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = AzulTechFlex,
            modifier = Modifier
                //Activa el modal
                .clickable { ModalRecuperar = true }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        //Registro
        Text(
            text = "¿No tienes cuenta? Regístrate",
            modifier = Modifier
                .clickable { /* Logica de nav al registro aqui. */ }
                .padding(8.dp)
        )

        //Logica del modal
        if (ModalRecuperar) {
            AlertDialog(
                onDismissRequest = {
                    ModalRecuperar = false // Se cierra si el toque se registra fuera del cuadro
                },
                title = {
                    Text(text = "Recuperar Contraseña")
                },
                text = {
                    Column {
                        Text(
                            text = "Ingrese su correo electrónico para recibir un enlace de recuperación.",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        // Input para el modal
                        OutlinedTextField(
                            value = correoRecuperacion,
                            onValueChange = { correoRecuperacion = it },
                            label = { Text("Correo de recuperación") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Aqui iria la conexion real con un correo en produccion.
                            ModalRecuperar = false
                            correoRecuperacion = "" //Cierra modal y limpia input.
                        },
                        // El modal solo es enviable por ahora si contiene un @, esto luego sera cambiado por si el correo coincide con el registrado en db.
                        enabled = correoRecuperacion.contains("@")
                    ) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            ModalRecuperar = false
                            correoRecuperacion = "" // Limpia al cancelar
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

}


