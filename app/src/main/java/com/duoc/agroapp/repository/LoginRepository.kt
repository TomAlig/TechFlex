package com.duoc.agroapp.repository

import com.duoc.agroapp.data.remote.RetrofitClient
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

    //Baja los usuarios de xano y los guarda en room sin perder la sesion recordada
    suspend fun sincronizarConXano() {
        //Se anota quien tenia la sesion antes de reemplazar los datos
        val emailSesion = userDao.buscarEmailSesion()
        val usuariosXano = RetrofitClient.usuarioApi.obtenerUsuarios()
        //La sesion recordada es de cada telefono, por eso se marca aca y no viene de xano
        userDao.guardarTodosLosUsuarios(
            usuariosXano.map { it.copy(esUltimoLogeado = it.email == emailSesion) }
        )
    }

    //Funcion de inicio de sesion (mas robusta, sin simulacion.)
    suspend fun iniciarSesion(email: String, contrasena: String, mantener: Boolean): LoginResultado {
        try {
            sincronizarConXano()
        } catch (e: Exception) {
            //Si no hay internet se sigue con los usuarios que ya estan guardados en room
        }
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

    //Cerrar sesion (en gestor de perfil)
    suspend fun cerrarSesion() {
        userDao.desmarcarUltimosUsuarios()
    }
}