package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.data.remote.RetrofitClient

//Une el ViewModel del registro con xano y el Dao de usuarios
class RegistroRepository(private val userDao: UserDao) {

    //Registra al usuario en xano y despues en room
    //Devuelve el mensaje de error o null si se registro bien
    suspend fun registrar(usuario: UserEntity): String? {
        try {
            //Los repetidos se revisan en xano porque ahi estan los usuarios de todos los telefonos
            val usuariosXano = RetrofitClient.usuarioApi.obtenerUsuarios()
            if (usuariosXano.any { it.email == usuario.email }) {
                return "Ya existe una cuenta con este correo"
            }
            if (usuariosXano.any { it.rut == usuario.rut }) {
                return "Ya existe una cuenta con este RUT"
            }

            //Primero se crea en xano y despues se guarda en room con el id que dio xano
            val creadoEnXano = RetrofitClient.usuarioApi.crearUsuario(usuario)
            userDao.registrarUsuario(usuario.copy(id = creadoEnXano.id))
            return null
        } catch (e: Exception) {
            //Si no hay internet o xano falla la app no se cae, solo muestra el mensaje
            return "No se pudo conectar con el servidor, intenta de nuevo"
        }
    }
}