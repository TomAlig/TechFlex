package com.duoc.agroapp.ui.screens

import android.widget.Toast
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.duoc.agroapp.viewmodel.PerfilViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun PerfilUsuarioScreen(viewmodel: PerfilViewModel, onCerrarSesion: () -> Unit,  onVolver: () -> Unit) {

    //Context para toast (mensaje en respuesta a los cambios del perfil).)
    val context = LocalContext.current

    //Variables para la edicion de datos, por lo pronto solo el nombre y la contraseña.
    var nombreInput by remember { mutableStateOf("") }
    var contrasenaInput by remember { mutableStateOf("") }

    //viewmodel toma el valor de usuario desde PerfilViewModel y monitorea su estado.
    val usuarioBaseDatos by viewmodel.usuario.observeAsState()

    LaunchedEffect(usuarioBaseDatos) { // Apunta a usuarioBaseDatos para verificar que los datos lleguen.
        usuarioBaseDatos?.let { usuario ->
            nombreInput = usuario.nombre        // En cuanto llega el dato real, cambia
            contrasenaInput = usuario.contrasena // lo mete a las variables de la UI
        }
    }

    //Si existe, dibuja el resto.

    usuarioBaseDatos?.let { usuarioReal ->
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
                    onClick = onVolver,
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
                Text(
                    text = "Nombre (Editable)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = nombreInput,
                    onValueChange = { nombreInput = it }, // Actualiza la variable remember al escribir
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                Text(
                    text = "Contraseña (Editable)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = contrasenaInput,
                    onValueChange = { contrasenaInput = it },
                    visualTransformation = PasswordVisualTransformation(), // Censura la contraseña vieja. (Actualmente sin confirmacion.)
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                Text(
                    text = "Correo electrónico",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuarioReal.email, //Pasa el correo real desde el usuario en room
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
                    text = usuarioReal.tipoUsuario.ifEmpty { "No especificado" },
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
                    onClick = {
                        val usuarioModificado = usuarioReal.copy( //Envía los cambios a la DB.
                            nombre = nombreInput,
                            contrasena = contrasenaInput
                        )
                        viewmodel.editarDatosUsuario(usuarioModificado)
                        Toast.makeText(context, "Cambios guardados con éxito.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Guardar Cambios", color = Color.White)
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
}