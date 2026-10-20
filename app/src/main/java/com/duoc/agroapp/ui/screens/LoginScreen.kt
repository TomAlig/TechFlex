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
import com.duoc.agroapp.R
import androidx.compose.ui.text.font.FontWeight
val AzulTechFlex = Color(0xFF3668FF)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // con el valor de estado se va actualizando por cada input.
    val estado = viewModel.estadoUI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tecmundo), //Foto de drawable del logo (png)
            contentDescription = "Logo Tecmundo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Fit
        )
        Text(text = "CONNECT", fontSize = 20.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.offset(y = (-95).dp))
        Spacer(modifier = Modifier.height(10.dp))

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
            colors =  OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulTechFlex,
                focusedLabelColor = AzulTechFlex,
                cursorColor = AzulTechFlex
            )

        )
        Spacer(modifier = Modifier.height(24.dp))

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
    }
}