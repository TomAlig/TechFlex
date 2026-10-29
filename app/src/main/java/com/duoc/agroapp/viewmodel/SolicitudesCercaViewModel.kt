package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.SolicitudCercana
import com.duoc.agroapp.model.SolicitudesCercaEstadoUI
import com.duoc.agroapp.repository.SolicitudRepository
import kotlinx.coroutines.launch

//Escucha las solicitudes abiertas y las prepara para la vista del prestador.
class SolicitudesCercaViewModel(
    private val repository: SolicitudRepository
) : ViewModel() {

    var estadoUI by mutableStateOf(SolicitudesCercaEstadoUI())
        private set

    init {
        //Cada vez que un productor crea o elimina una solicitud, esta lista se actualiza sola.
        viewModelScope.launch {
            repository.obtenerSolicitudesAbiertas().collect { lista ->
                val cercanas = lista
                    .map { convertir(it) }
                    .sortedBy { it.distanciaKm }   //la más cercana primero
                estadoUI = estadoUI.copy(solicitudes = cercanas, cargando = false)
            }
        }
    }

    //Pasa de la fila de la tabla (SolicitudEntity) a lo que muestra la tarjeta (SolicitudCercana).
    private fun convertir(solicitud: SolicitudEntity): SolicitudCercana {
        return SolicitudCercana(
            id = solicitud.id,
            tipoProducto = solicitud.tipoProducto,
            superficieHa = solicitud.superficieHa,
            aplicacion = solicitud.aplicacion,
            //"Antenas,Cables" -> ["Antenas", "Cables"]. El filter descarta el texto vacío si no hay obstáculos.
            obstaculos = solicitud.obstaculos.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            distanciaKm = distanciaSimulada(solicitud.id)
        )
    }

    //Distancia de prueba mientras no usemos el GPS (entre 5 y 49 km).
    //Depende del id para que no cambie cada vez que se abre la pantalla.
    private fun distanciaSimulada(id: Int): Int = (id * 13) % 45 + 5
}