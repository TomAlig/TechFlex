package com.duoc.agroapp.model

//Oferta ficticia de un prestador de servicios agrícolas (datos de demostración).
data class OfertaEjemplo(
    val id: Int,
    val nombrePrestador: String,
    val calificacion: Double,
    val precioPorHa: Int,
    val descripcion: String
)

//Estado de la pantalla "Ofertas".
data class OfertasEstadoUI(
    val ofertas: List<OfertaEjemplo> = emptyList(),
    //Ids de los prestadores marcados con la estrella.
    val favoritosIds: Set<Int> = emptySet(),
    //Oferta que el usuario quiere aceptar; si no es null se muestra el modal de confirmación.
    val ofertaPorAceptar: OfertaEjemplo? = null,
    //Id de la oferta aceptada (null si todavía no acepta ninguna).
    val ofertaAceptadaId: Int? = null,
    //Prestador al que se "envió" un mensaje de contacto; si no es null se muestra el modal.
    val ofertaContactada: OfertaEjemplo? = null
)