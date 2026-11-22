package com.duoc.agroapp.data.remote

import com.duoc.agroapp.data.local.SolicitudEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

//Define las operaciones realizables hacia el backend y que datos Json se mueven. (Como el DAO en Room)
//ApiService, se construye en funcion a las funciones utilizadas en su respectivo repository, con la añadidura de un fetch (en repository) para actualizar room con la nube.
interface SolicitudApiService {
    @GET("solicitudes")
    suspend fun obtenerSolicitudes(): List<SolicitudEntity>
    @POST("solicitudes")
    suspend fun crearSolicitud(@Body solicitud: SolicitudEntity): SolicitudEntity

    @DELETE("solicitudes/{solicitudes_id}")
    suspend fun eliminarSolicitud(@Path("solicitudes_id") id: Int)

    //Actualizacion de estado tras cambio en oferta.
    @PATCH("solicitudes/{solicitudes_id}")
    suspend fun actualizarSolicitud(
        @Path("solicitudes_id") id: Int,
        @Body solicitud: SolicitudEntity
    ): SolicitudEntity
}