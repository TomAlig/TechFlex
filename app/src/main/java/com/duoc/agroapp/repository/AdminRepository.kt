package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.model.TIPO_ADMIN
import kotlinx.coroutines.flow.Flow
import com.duoc.agroapp.data.remote.RetrofitClient

//Une la vista del administrador con la tabla de usuarios.
class AdminRepository(private val userDao: UserDao) {

    //Datos de la cuenta admin fija. Están en un solo lugar por si hay que cambiarlos.
    companion object {
        const val EMAIL_ADMIN = "admin@tecmundo.cl"
        const val CONTRASENA_ADMIN = "admin1234"
    }

    //Baja los usuarios de xano y los guarda en room sin perder la sesion recordada
    suspend fun sincronizarConXano() {
        val emailSesion = userDao.buscarEmailSesion()
        val usuariosXano = RetrofitClient.usuarioApi.obtenerUsuarios()
        userDao.guardarTodosLosUsuarios(
            usuariosXano.map { it.copy(esUltimoLogeado = it.email == emailSesion) }
        )
    }
    //Borra al usuario en xano y despues en room
    suspend fun eliminarUsuario(usuario: UserEntity) {
        //Si el id es 0 el usuario solo existe en este telefono, asi que no se llama a xano
        if (usuario.id != 0) {
            RetrofitClient.usuarioApi.eliminarUsuario(usuario.id)
        }
        userDao.eliminarUsuario(usuario)
    }

    //Lista de usuarios (sin el admin), se actualiza sola al eliminar.
    fun obtenerUsuarios(): Flow<List<UserEntity>> {
        return userDao.obtenerUsuariosParaAdmin()
    }

    //Crea la cuenta admin solo si todavía no existe. Se llama cada vez que se abre la app.
    suspend fun crearAdminSiNoExiste() {
        if (userDao.buscarPorEmail(EMAIL_ADMIN) == null) {
            userDao.registrarUsuario(
                UserEntity(
                    email = EMAIL_ADMIN,
                    contrasena = CONTRASENA_ADMIN,
                    nombre = "Administrador",
                    tipoUsuario = TIPO_ADMIN
                )
            )
        }
    }
}