package com.duoc.agroapp.model

import com.duoc.agroapp.data.local.SolicitudEntity

const val OBSTACULO_OTROS = "Otros"
val TIPOS_PRODUCTO = listOf("Granulada", "Líquida")
val OPCIONES_APLICACION = listOf("Diurna", "Nocturna")
val OBSTACULOS_DISPONIBLES = listOf("Antenas", "Cables", "Torres de alta tensión", OBSTACULO_OTROS)

const val ESTADO_PENDIENTE = "Pendiente"
const val ESTADO_CON_OFERTAS = "Con ofertas"
const val ESTADO_FINALIZADA = "Finalizada"

//Estado completo del formulario "Crear solicitud".
data class CrearSolicitudEstadoUI(
    val tipoProducto: String = "",
    val superficie: String = "",
    val tieneAgua: Boolean = true,
    val aplicacion: String = "Diurna",
    val obstaculos: Set<String> = emptySet(),
    val comentarios: String = "",
    val fotoUri: String? = null,

    //Un error por campo (null = campo válido).
    val errorTipoProducto: String? = null,
    val errorSuperficie: String? = null,
    val errorComentarios: String? = null,
    val errorFoto: String? = null,
    val mostrarDialogoConfirmacion: Boolean = false,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)

//Estado de la pantalla "Mis solicitudes".
data class MisSolicitudesEstadoUI(
    val solicitudes: List<SolicitudEntity> = emptyList(),
    val cargando: Boolean = true,
    //Solicitud que el usuario quiere eliminar; si no es null se muestra el modal de confirmación.
    val solicitudAEliminar: SolicitudEntity? = null
)