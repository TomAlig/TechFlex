package com.duoc.agroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Tabla de solicitudes de servicio creadas por un productor.
@Entity(tableName = "solicitudes_tabla")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //Correo del productor dueño de la solicitud (viene del login).
    val emailUsuario: String,
    val tipoProducto: String,
    val superficieHa: Double,
    val tieneAgua: Boolean,
    val aplicacion: String,
    //Obstáculos separados por coma, ej: "Antenas,Cables".
    val obstaculos: String,
    val comentarios: String,
    //Reservado para la foto del terreno (cámara, se implementa después).
    val fotoUri: String? = null,
    val estado: String = "Pendiente"
)