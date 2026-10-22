package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

//Union de viewmodel y Dao
//El repository toma el dao como argumento para operar.
class LoginRepository(private val userDao: UserDao) {
    val ultimoUsuario: Flow<UserEntity ?> = userDao.obtenerUltimoUsuario()

    //Simula el login.
    suspend fun simularLoginExitoso(email: String, contrasena: String) {
        //Se limpia sesion vieja
        userDao.desmarcarUltimosUsuarios()

        //Se crea una nueva entidad y el dao la guarda en el disco duro.
        val usuario = UserEntity(email = email, contrasena = contrasena, esUltimoLogeado = true)
        userDao.guardarUsuario(usuario)
    }
}