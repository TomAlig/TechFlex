package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.SolicitudDao
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

//Une los ViewModels con el Dao de solicitudes.
class SolicitudRepository(private val solicitudDao: SolicitudDao) {

    suspend fun crearSolicitud(solicitud: SolicitudEntity) {
        //Primero la crea en Xano, luego esa se guarda local pero con la misma ID.
        val creadaEnXano = RetrofitClient.solicitudApi.crearSolicitud(solicitud)
        solicitudDao.guardarSolicitud(solicitud.copy(id = creadaEnXano.id))
    }

    fun obtenerSolicitudes(emailUsuario: String): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerSolicitudesDeUsuario(emailUsuario)
    }

    //Solicitudes abiertas de todos los productores, para la vista del prestador.
    fun obtenerSolicitudesAbiertas(): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerSolicitudesAbiertas()
    }

    /* Esta funcion y la de creacion deben actualizar xano y room por separado, las de lectura
    agarran xano tal cual estan gracias a que viewmodel actualiza room al iniciar. */
    suspend fun eliminarSolicitud(solicitud: SolicitudEntity) {
        RetrofitClient.solicitudApi.eliminarSolicitud(solicitud.id)
        solicitudDao.eliminarSolicitud(solicitud)
    }

    //Funcion extra para hacer un fetch al xano y actualizar room, asi funciona local tambien la app.
    suspend fun sincronizarConXano() {
        val listaDeXano = RetrofitClient.solicitudApi.obtenerSolicitudes()
        solicitudDao.guardarTodasLasSolicitudes(listaDeXano)
    }


}