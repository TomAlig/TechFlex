package com.duoc.agroapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    //Guarda una nueva solicitud en el disco del celular.
    @Insert
    suspend fun guardarSolicitud(solicitud: SolicitudEntity)

    //Solo las solicitudes del usuario indicado, las más nuevas primero. Se actualiza sola al cambiar la tabla.
    @Query("SELECT * FROM solicitudes_tabla WHERE emailUsuario = :email ORDER BY id DESC")
    fun obtenerSolicitudesDeUsuario(email: String): Flow<List<SolicitudEntity>>

    @Delete
    suspend fun eliminarSolicitud(solicitud: SolicitudEntity)
}