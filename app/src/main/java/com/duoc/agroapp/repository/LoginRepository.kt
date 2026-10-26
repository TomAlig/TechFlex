package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.model.LoginResultado
import kotlinx.coroutines.flow.Flow

//Union de viewmodel y Dao
//El repository toma el dao como argumento para operar.
class LoginRepository(private val userDao: UserDao) {

    suspend fun obtenerUltimoUsuario(): UserEntity? {
        return userDao.obtenerUltimoUsuario()
    }

    //Cerrar sesion (en gestor de perfil)
    suspend fun iniciarSesion(email: String, contrasena: String, mantener: Boolean): LoginResultado {
        val usuario = userDao.buscarPorEmail(email)
            ?: return LoginResultado(
                exito = false,
                errorMsg = "No existe una cuenta con este correo"
            )

        if (usuario.contrasena != contrasena) {
            return LoginResultado(exito = false, errorMsg = "Contraseña incorrecta")
        }

        //Se limpia la sesión anterior, si "Recuérdame" esta marcado, se recuerda a este usuario.
        userDao.desmarcarUltimosUsuarios()
        if (mantener) userDao.marcarComoLogeado(email)
        return LoginResultado(exito = true)
    }

    suspend fun cerrarSesion() {
        userDao.desmarcarUltimosUsuarios()
    }
}