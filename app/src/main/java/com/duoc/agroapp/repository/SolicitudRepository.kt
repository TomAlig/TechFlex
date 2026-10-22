package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.SolicitudDao
import com.duoc.agroapp.data.local.SolicitudEntity
import kotlinx.coroutines.flow.Flow

//Une los ViewModels con el Dao de solicitudes.
class SolicitudRepository(private val solicitudDao: SolicitudDao) {

    suspend fun crearSolicitud(solicitud: SolicitudEntity) {
        solicitudDao.guardarSolicitud(solicitud)
    }

    fun obtenerSolicitudes(emailUsuario: String): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerSolicitudesDeUsuario(emailUsuario)
    }

    suspend fun eliminarSolicitud(solicitud: SolicitudEntity) {
        solicitudDao.eliminarSolicitud(solicitud)
    }
}