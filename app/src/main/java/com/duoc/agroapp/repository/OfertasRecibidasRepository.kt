package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.OfertaDao
import com.duoc.agroapp.data.local.OfertaEntity
import com.duoc.agroapp.data.local.SolicitudDao
import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.model.ESTADO_FINALIZADA
import com.duoc.agroapp.model.OfertaConPrestador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Une las ofertas recibidas con los datos de cada prestador, para la vista del productor.
class OfertasRecibidasRepository(
    private val ofertaDao: OfertaDao,
    private val solicitudDao: SolicitudDao,
    private val userDao: UserDao
) {

    //Ofertas de una solicitud, de la más barata a la más cara. Se actualiza sola al cambiar la tabla.
    fun obtenerOfertas(solicitudId: Int): Flow<List<OfertaConPrestador>> {
        return ofertaDao.obtenerOfertasDeSolicitud(solicitudId).map { ofertas ->
            ofertas.map { oferta ->
                val prestador = userDao.buscarPorEmail(oferta.emailPrestador)
                OfertaConPrestador(
                    oferta = oferta,
                    //Si el prestador no tiene nombre guardado, se muestra su correo.
                    nombrePrestador = prestador?.nombre?.ifBlank { oferta.emailPrestador } ?: oferta.emailPrestador,
                    telefonoPrestador = prestador?.telefono ?: ""
                )
            }
        }
    }

    //Acepta una oferta, rechaza las demás y deja la solicitud como finalizada.
    suspend fun aceptarOferta(oferta: OfertaEntity) {
        ofertaDao.marcarAceptada(oferta.id)
        ofertaDao.rechazarLasDemas(oferta.solicitudId, oferta.id)
        solicitudDao.actualizarEstado(oferta.solicitudId, ESTADO_FINALIZADA)
    }
}