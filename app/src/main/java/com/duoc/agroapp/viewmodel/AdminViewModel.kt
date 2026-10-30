package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.model.AdminEstadoUI
import com.duoc.agroapp.repository.AdminRepository
import kotlinx.coroutines.launch

//Muestra los usuarios registrados y permite eliminarlos con confirmación.
class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    var estadoUI by mutableStateOf(AdminEstadoUI())
        private set

    init {
        //Cada vez que la tabla cambia (ej: se elimina alguien), la lista se actualiza sola.
        viewModelScope.launch {
            repository.obtenerUsuarios().collect { lista ->
                estadoUI = estadoUI.copy(usuarios = lista, cargando = false)
            }
        }
    }

    //Primer toque: guarda a quién se quiere eliminar y abre el modal.
    fun onEliminarClick(usuario: UserEntity) {
        estadoUI = estadoUI.copy(usuarioAEliminar = usuario)
    }

    fun onCancelarEliminar() {
        estadoUI = estadoUI.copy(usuarioAEliminar = null)
    }

    //Segundo toque (en el modal): elimina de verdad y cierra el modal.
    fun onConfirmarEliminar() {
        val usuario = estadoUI.usuarioAEliminar ?: return
        viewModelScope.launch {
            repository.eliminarUsuario(usuario)
            estadoUI = estadoUI.copy(usuarioAEliminar = null)
        }
    }
}