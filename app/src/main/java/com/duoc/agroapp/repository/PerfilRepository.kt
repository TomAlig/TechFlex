package com.duoc.agroapp.repository

import androidx.lifecycle.LiveData
import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity

//NOTA: En el mejor de los casos para la proxima version, hay que mover cerrarSesion aca, o unificar los repositories en un solo UserRepository.
class PerfilRepository (private val userDao: UserDao) {
    // Para el botón de Editar
    suspend fun actualizarUsuario(usuario: UserEntity) {
        userDao.actualizarUsuario(usuario)
    }

    //TESTINGRESO
    fun obtenerUsuarioPorEmail(email: String): LiveData<UserEntity?> {
        return userDao.obtenerUsuarioPorEmailLiveData(email)
    }


}