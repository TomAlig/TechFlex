package com.duoc.agroapp.repository

import androidx.lifecycle.LiveData
import com.duoc.agroapp.data.local.UserDao
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.data.remote.RetrofitClient

//NOTA: En el mejor de los casos para la proxima version, hay que mover cerrarSesion aca, o unificar los repositories en un solo UserRepository.
class PerfilRepository (private val userDao: UserDao) {
    // Para el botón de Editar
    suspend fun actualizarUsuario(usuario: UserEntity) {
        val actualizadoEnXano = RetrofitClient.usuarioApi.actualizarUsuario(
            id = usuario.id,
            usuario = usuario
        )
        userDao.actualizarUsuario(
            actualizadoEnXano.copy(esUltimoLogeado = usuario.esUltimoLogeado)
        )
    }

    //TESTINGRESO
    fun obtenerUsuarioPorEmail(email: String): LiveData<UserEntity?> {
        return userDao.obtenerUsuarioPorEmailLiveData(email)
    }
}