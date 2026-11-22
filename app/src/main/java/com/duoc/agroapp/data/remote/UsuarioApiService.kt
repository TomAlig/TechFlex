package com.duoc.agroapp.data.remote

import com.duoc.agroapp.data.local.UserEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

//Operaciones de la tabla usuarios en xano, funciona como un dao pero hacia la nube
interface UsuarioApiService {

    //Trae todos los usuarios
    @GET("usuario")
    suspend fun obtenerUsuarios(): List<UserEntity>

    //Crea un usuario nuevo y xano lo devuelve con su id
    @POST("usuario")
    suspend fun crearUsuario(@Body usuario: UserEntity): UserEntity

    //Edita un usuario usando el id de xano
    @PATCH("usuario/{usuario_id}")
    suspend fun actualizarUsuario(
        @Path("usuario_id") id: Int,
        @Body usuario: UserEntity
    ): UserEntity

    //Borra un usuario usando el id de xano
    @DELETE("usuario/{usuario_id}")
    suspend fun eliminarUsuario(@Path("usuario_id") id: Int)
}