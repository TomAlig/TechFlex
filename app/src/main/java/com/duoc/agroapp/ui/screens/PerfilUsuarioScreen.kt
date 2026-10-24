package com.duoc.agroapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PerfilUsuarioScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil de Usuario")
        Text("Correo@gmail.com")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCerrarSesion,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            launchSingleTop = true
        ) {
            Text("Cerrar Sesión")
        }
    }
}