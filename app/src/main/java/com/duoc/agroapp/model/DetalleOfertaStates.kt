package com.duoc.agroapp.model

import com.duoc.agroapp.data.local.SolicitudEntity
import kotlin.math.roundToLong

//Estado completo de la pantalla "Detalle de solicitud" del prestador.
data class DetalleOfertaEstadoUI(
    //null mientras carga, o si la solicitud ya no existe.
    val solicitud: SolicitudEntity? = null,
    val cargando: Boolean = true,
    //Lo que escribe el prestador en "Ingresar precio por ha" (solo números).
    val precioTexto: String = "",
    val errorPrecio: String? = null,
    //Error que no es del campo, ej: "Ya enviaste una oferta para esta solicitud".
    val errorGeneral: String? = null,
    val enviando: Boolean = false,
    //true si el prestador ya ofertó en esta solicitud (antes o recién).
    val ofertaEnviada: Boolean = false
) {
    //Precio total = precio por ha × superficie de la solicitud.
    //Se recalcula solo cada vez que cambia el precio. Es null si todavía no hay un precio válido.
    val precioTotal: Long?
        get() {
            val precio = precioTexto.toLongOrNull() ?: return null
            val superficie = solicitud?.superficieHa ?: return null
            return (precio * superficie).roundToLong()
        }
}