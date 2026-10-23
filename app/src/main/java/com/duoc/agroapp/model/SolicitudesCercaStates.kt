package com.duoc.agroapp.model

//Lo que muestra cada tarjeta de "Solicitudes cerca de ti".
//No es una tabla de Room: se arma a partir de SolicitudEntity + la distancia al prestador.
data class SolicitudCercana(
    val id: Int,
    val tipoProducto: String,        //"Granulada" o "Líquida"
    val superficieHa: Double,
    val aplicacion: String,          //"Diurna" o "Nocturna"
    val obstaculos: List<String>,    //ej: listOf("Cables", "Antenas")
    val distanciaKm: Int
)

//Estado completo de la pantalla "Solicitudes cerca".
data class SolicitudesCercaEstadoUI(
    val solicitudes: List<SolicitudCercana> = emptyList(),
    //Empieza en true porque al abrir la pantalla todavía no llegan los datos.
    val cargando: Boolean = true
)