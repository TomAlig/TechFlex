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
    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<UserEntity>

    //Crea un usuario nuevo y xano lo devuelve con su id
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: UserEntity): UserEntity

    //Edita un usuario usando el id de xano
    @PATCH("usuarios/{usuarios_id}")
    suspend fun actualizarUsuario(
        @Path("usuarios_id") id: Int,
        @Body usuario: UserEntity
    ): UserEntity

    //Borra un usuario usando el id de xano
    @DELETE("usuarios/{usuarios_id}")
    suspend fun eliminarUsuario(@Path("usuarios_id") id: Int)
}