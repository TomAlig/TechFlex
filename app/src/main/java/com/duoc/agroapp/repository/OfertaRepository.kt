package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.OfertaDao
import com.duoc.agroapp.data.local.OfertaEntity
import com.duoc.agroapp.data.local.SolicitudDao
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.ESTADO_CON_OFERTAS

//Une el ViewModel del Detalle con las tablas de ofertas y solicitudes.
class OfertaRepository(
    private val ofertaDao: OfertaDao,
    private val solicitudDao: SolicitudDao
) {

    //La solicitud que se muestra en el Detalle.
    suspend fun obtenerSolicitud(id: Int): SolicitudEntity? {
        return solicitudDao.obtenerSolicitudPorId(id)
    }
    //true si este prestador ya ofertó en esta solicitud.
    suspend fun yaOferto(solicitudId: Int, emailPrestador: String): Boolean {
        return ofertaDao.buscarOfertaDelPrestador(solicitudId, emailPrestador) != null
    }

    //Guarda la oferta y marca la solicitud como "Con ofertas".
    //Igual que en el registro: devuelve el mensaje de error, o null si salió bien.
    suspend fun enviarOferta(oferta: OfertaEntity): String? {
        if (yaOferto(oferta.solicitudId, oferta.emailPrestador)) {
            return "Ya enviaste una oferta para esta solicitud"
        }
        ofertaDao.guardarOferta(oferta)
        solicitudDao.actualizarEstado(oferta.solicitudId, ESTADO_CON_OFERTAS)
        return null
    }
}