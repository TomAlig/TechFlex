package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.OfertaDao
import com.duoc.agroapp.data.local.OfertaEntity
import com.duoc.agroapp.data.local.SolicitudDao
import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.remote.RetrofitClient
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
        //Marca aceptada en xano y room.
        RetrofitClient.ofertaApi.actualizarOferta(id = oferta.id, oferta = oferta.copy(estado = "Aceptada"))
        ofertaDao.marcarAceptada(oferta.id)

        //Marca las demás ofertas de esa solicitud con rechazada en xano y luego en room.
        //Actualiza el resto de ofertas matcheando el id para dejarlas en rechazada
        val otrasOfertas = RetrofitClient.ofertaApi.obtenerOfertas()
            .filter { it.solicitudId == oferta.solicitudId && it.id != oferta.id }
        otrasOfertas.forEach { otra ->
            RetrofitClient.ofertaApi.actualizarOferta(
                id = otra.id,
                oferta = otra.copy(estado = "Rechazada")
            )
        }
        //Lo mismo en room
        ofertaDao.rechazarLasDemas(oferta.solicitudId, oferta.id)

        //Cambia la solicitud a finalizada para que no aparezca en xano y luego en room
        val solicitud = solicitudDao.obtenerSolicitudPorId(oferta.solicitudId)
        if (solicitud != null) {
            RetrofitClient.solicitudApi.actualizarSolicitud(
                id = oferta.solicitudId,
                solicitud = solicitud.copy(estado = ESTADO_FINALIZADA)
            )
            solicitudDao.actualizarEstado(oferta.solicitudId, ESTADO_FINALIZADA)
        }
    }

    suspend fun sincronizarConXano() {
        val listaDeXano = RetrofitClient.ofertaApi.obtenerOfertas()
        ofertaDao.guardarTodasLasOfertas(listaDeXano)
    }
}