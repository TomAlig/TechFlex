package com.duoc.agroapp.ui.components

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.duoc.agroapp.ui.screens.AzulTechFlex
import java.io.File

//Toma una foto con la cámara y la muestra. Avisa con la ruta (uri) de la foto tomada.
@Composable
fun CamaraFotos(
    onFotoCapturada: (String) -> Unit,
    onPermisoDenegado: () -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzador de cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = imageUri
        if (success && uri != null) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            onFotoCapturada(uri.toString())
        }
    }

    // Permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File.createTempFile("foto_solicitud", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )
            imageUri = uri
            takePictureLauncher.launch(uri)
        } else {
            onPermisoDenegado()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bitmap?.let { foto ->
            Image(
                bitmap = foto.asImageBitmap(),
                contentDescription = "Foto del terreno",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
            colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
        ) {
            Text(if (bitmap == null) "Tomar foto del terreno" else "Tomar otra foto")
        }
    }
}