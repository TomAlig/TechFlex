package com.duoc.agroapp.model

import com.duoc.agroapp.data.local.OfertaEntity

const val OFERTA_PENDIENTE = "Pendiente"
const val OFERTA_ACEPTADA = "Aceptada"
const val OFERTA_RECHAZADA = "Rechazada"

//Una oferta guardada junto con los datos del prestador que la hizo.
data class OfertaConPrestador(
    val oferta: OfertaEntity,
    val nombrePrestador: String,
    val telefonoPrestador: String
)

//Estado de la pantalla "Ofertas".
data class OfertasEstadoUI(
    val ofertas: List<OfertaConPrestador> = emptyList(),
    val cargando: Boolean = true,
    //Ids de las ofertas marcadas con la estrella (solo en memoria).
    val favoritosIds: Set<Int> = emptySet(),
    //Oferta que el usuario quiere aceptar; si no es null se muestra el modal de confirmación.
    val ofertaPorAceptar: OfertaConPrestador? = null,
    //Prestador cuyos datos de contacto se muestran en el modal.
    val ofertaContactada: OfertaConPrestador? = null
)