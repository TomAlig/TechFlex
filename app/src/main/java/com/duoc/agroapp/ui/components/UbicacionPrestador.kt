package com.duoc.agroapp.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.duoc.agroapp.ui.screens.AzulTechFlex
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

//Boton que pide permiso de ubicacion y lee el gps del prestador
@Composable
fun UbicacionPrestador(
    onUbicacionObtenida: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current

    //Pide los permisos de ubicacion al usuario, igual que el permiso de la camara
    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        //Basta con que acepte uno de los dos, el preciso o el aproximado
        val aceptado = permisos.values.any { it }
        if (aceptado) {
            leerUbicacion(context, onUbicacionObtenida, onError)
        } else {
            onError("Sin permiso de ubicación no podemos saber dónde estás")
        }
    }

    Button(
        onClick = {
            permisoLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        colors = ButtonDefaults.buttonColors(containerColor = AzulTechFlex)
    ) {
        Icon(Icons.Filled.LocationOn, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text("Usar mi ubicación")
    }
}

//Lee la ubicacion actual con la libreria de google
//El SuppressLint es porque esta funcion solo se llama despues de que el usuario dio el permiso
@SuppressLint("MissingPermission")
private fun leerUbicacion(
    context: Context,
    onUbicacionObtenida: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val cliente = LocationServices.getFusedLocationProviderClient(context)
    cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { ubicacion ->
            if (ubicacion != null) {
                onUbicacionObtenida(ubicacion.latitude, ubicacion.longitude)
            } else {
                onError("No se pudo leer la ubicación, revisa que el GPS esté activado")
            }
        }
        .addOnFailureListener {
            onError("No se pudo leer la ubicación, revisa que el GPS esté activado")
        }
}