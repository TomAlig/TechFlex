package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

//Union de viewmodel y Dao
//El repository toma el dao como argumento para operar.
class LoginRepository(private val userDao: UserDao) {

    suspend fun obtenerUltimoUsuario(): UserEntity? {
        return userDao.obtenerUltimoUsuario()
    }
    //Simula el login.
    suspend fun simularLoginExitoso(email: String, contrasena: String, mantener: Boolean) {
        //Se limpia sesion vieja
        userDao.desmarcarUltimosUsuarios()

        if (mantener) {
            // Se guarda al usuario como recordado.
        val usuario = UserEntity(email = email, contrasena = contrasena, esUltimoLogeado = true)
        userDao.guardarUsuario(usuario)
        }
    }
}