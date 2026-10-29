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

    //Todas las solicitudes que todavía aceptan ofertas, de cualquier productor.
    //La usa la vista "Solicitudes cerca" del prestador.
    @Query("SELECT * FROM solicitudes_tabla WHERE estado != 'Finalizada' ORDER BY id DESC")
    fun obtenerSolicitudesAbiertas(): Flow<List<SolicitudEntity>>

    //Una sola solicitud por su id, para la vista de Detalle.
    //Es Flow: si cambia su estado, la pantalla se actualiza sola.
    @Query("SELECT * FROM solicitudes_tabla WHERE id = :id")
    fun obtenerSolicitudPorId(id: Int): Flow<SolicitudEntity?>

    //Cambia el estado de una solicitud (ej: de "Pendiente" a "Con ofertas").
    @Query("UPDATE solicitudes_tabla SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)
    @Delete
    suspend fun eliminarSolicitud(solicitud: SolicitudEntity)
}