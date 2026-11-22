package com.duoc.agroapp.data.remote

import com.duoc.agroapp.data.local.OfertaEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OfertaApiService {
    @GET("oferta")
    suspend fun obtenerOfertas(): List<OfertaEntity>

    @POST("oferta")
    suspend fun crearOferta(@Body oferta: OfertaEntity): OfertaEntity

    @PATCH("oferta/{oferta_id}")
    suspend fun actualizarOferta(
        @Path("oferta_id") id: Int,
        @Body oferta: OfertaEntity
    ): OfertaEntity
}