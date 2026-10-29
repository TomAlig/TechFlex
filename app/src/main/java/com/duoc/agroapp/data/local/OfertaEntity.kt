package com.duoc.agroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Tabla de ofertas que hacen los prestadores a una solicitud.
//La vista "Ofertas" del productor lee esta misma tabla.
@Entity(tableName = "ofertas_tabla")
data class OfertaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //A qué solicitud corresponde (el id de SolicitudEntity).
    val solicitudId: Int,
    //Correo del prestador que ofertó (viene del login).
    val emailPrestador: String,
    val precioPorHa: Long,
    //precioPorHa × superficie de la solicitud, sin IVA.
    val precioTotal: Long,
    val estado: String = "Pendiente"     //"Pendiente", "Aceptada" o "Rechazada"
)