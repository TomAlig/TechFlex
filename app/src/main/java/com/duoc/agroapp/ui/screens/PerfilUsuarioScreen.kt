package com.duoc.agroapp.ui.screens

import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.duoc.agroapp.R
import androidx.compose.material3.Icon

@Composable
fun PerfilUsuarioScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Este row es para el logo y la flecha para volver atras.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Empuja a la izquierda
        ) {
            // Flecha volver (sin funcion)
            IconButton(
                onClick = { },
                modifier = Modifier.padding(end = 8.dp) // Separa la flecha del logo
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver atrás",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(150.dp))

            // El Logo de tu app al lado de la flecha
            Image(
                painter = painterResource(id = R.drawable.logo_tecmundo_connect),
                contentDescription = "Logo Tecmundo",
                modifier = Modifier
                    .height(40.dp) // Un tamaño más sutil para la esquina
                    .wrapContentWidth(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.placeholder_user),
            contentDescription = "Foto Perfil",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            //Placeholders de texto para antes de tener datos de registro.
            Text(
                text = "Nombre completo",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Juan Pérez",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Correo electrónico",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "usuario@tecmundo.com",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Tipo de Usuario",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Productor",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .width(300.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp) //los ordena horizontalmente.
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Editar Datos")
            }

            Button(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}