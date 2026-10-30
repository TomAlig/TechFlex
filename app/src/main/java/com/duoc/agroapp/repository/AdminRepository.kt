package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.model.TIPO_ADMIN
import kotlinx.coroutines.flow.Flow

//Une la vista del administrador con la tabla de usuarios.
class AdminRepository(private val userDao: UserDao) {

    //Datos de la cuenta admin fija. Están en un solo lugar por si hay que cambiarlos.
    companion object {
        const val EMAIL_ADMIN = "admin@tecmundo.cl"
        const val CONTRASENA_ADMIN = "admin1234"
    }

    //Lista de usuarios (sin el admin), se actualiza sola al eliminar.
    fun obtenerUsuarios(): Flow<List<UserEntity>> {
        return userDao.obtenerUsuariosParaAdmin()
    }

    suspend fun eliminarUsuario(usuario: UserEntity) {
        userDao.eliminarUsuario(usuario)
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