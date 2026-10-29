package com.duoc.agroapp.repository

import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity

//Une el ViewModel del registro con el Dao de usuarios.
class RegistroRepository(private val userDao: UserDao) {

    //Registra al usuario solo si su correo y su RUT no están ocupados.
    //Igual que los validadores: devuelve el mensaje de error, o null si se registró bien.
    suspend fun registrar(usuario: UserEntity): String? {
        if (userDao.buscarPorEmail(usuario.email) != null) {
            return "Ya existe una cuenta con este correo"
        }
        if (userDao.buscarPorRut(usuario.rut) != null) {
            return "Ya existe una cuenta con este RUT"
        }
        userDao.registrarUsuario(usuario)
        return null
    }
}