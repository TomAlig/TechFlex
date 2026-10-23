package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.ESTADO_PENDIENTE
import com.duoc.agroapp.model.MisSolicitudesEstadoUI
import com.duoc.agroapp.repository.SolicitudRepository
import kotlinx.coroutines.launch

//Escucha las solicitudes del usuario y maneja la eliminación con confirmación.
class MisSolicitudesViewModel(
    private val repository: SolicitudRepository,
    emailUsuario: String
) : ViewModel() {

    var estadoUI by mutableStateOf(MisSolicitudesEstadoUI())
        private set

    init {
        //Cada vez que la tabla cambia, la lista de la pantalla se actualiza sola.
        viewModelScope.launch {
            repository.obtenerSolicitudes(emailUsuario).collect { lista ->
                estadoUI = estadoUI.copy(solicitudes = lista, cargando = false)
            }
        }
    }

    //Regla de negocio: solo se puede eliminar una solicitud que aún no ha sido atendida (Pendiente).
    fun onEliminarClick(solicitud: SolicitudEntity) {
        if (solicitud.estado == ESTADO_PENDIENTE) {
            estadoUI = estadoUI.copy(solicitudAEliminar = solicitud)
        }
    }

    fun onCancelarEliminar() {
        estadoUI = estadoUI.copy(solicitudAEliminar = null)
    }

    fun onConfirmarEliminar() {
        val solicitud = estadoUI.solicitudAEliminar ?: return
        viewModelScope.launch {
            repository.eliminarSolicitud(solicitud)
            estadoUI = estadoUI.copy(solicitudAEliminar = null)
        }
    }
}