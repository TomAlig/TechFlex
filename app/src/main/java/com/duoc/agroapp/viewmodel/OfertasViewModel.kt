package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.model.OFERTA_PENDIENTE
import com.duoc.agroapp.model.OfertaConPrestador
import com.duoc.agroapp.model.OfertasEstadoUI
import com.duoc.agroapp.repository.OfertasRecibidasRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//Carga las ofertas de una solicitud y maneja favoritos, aceptación y contacto.
class OfertasViewModel(
    private val repository: OfertasRecibidasRepository
) : ViewModel() {

    var estadoUI by mutableStateOf(OfertasEstadoUI())
        private set

    //Escucha de la solicitud actual; se cancela al abrir otra solicitud.
    private var trabajoOfertas: Job? = null

    //Se llama cada vez que se abre la pantalla, con el id de la solicitud tocada.
    fun cargar(solicitudId: Int) {
        trabajoOfertas?.cancel()
        estadoUI = estadoUI.copy(ofertas = emptyList(), cargando = true)
        trabajoOfertas = viewModelScope.launch {
            //Cada vez que llega una oferta nueva o cambia una, la lista se actualiza sola.
            repository.obtenerOfertas(solicitudId).collect { lista ->
                estadoUI = estadoUI.copy(ofertas = lista, cargando = false)
            }
        }
    }

    //Marca o desmarca la estrella de una oferta.
    fun onFavoritoClick(idOferta: Int) {
        val nuevos = if (estadoUI.favoritosIds.contains(idOferta)) {
            estadoUI.favoritosIds - idOferta
        } else {
            estadoUI.favoritosIds + idOferta
        }
        estadoUI = estadoUI.copy(favoritosIds = nuevos)
    }

    //Regla de negocio: solo se puede aceptar una oferta que sigue pendiente.
    fun onAceptarClick(oferta: OfertaConPrestador) {
        if (oferta.oferta.estado == OFERTA_PENDIENTE) {
            estadoUI = estadoUI.copy(ofertaPorAceptar = oferta)
        }
    }

    fun onCancelarAceptar() {
        estadoUI = estadoUI.copy(ofertaPorAceptar = null)
    }

    fun onConfirmarAceptar() {
        val oferta = estadoUI.ofertaPorAceptar ?: return
        viewModelScope.launch {
            repository.aceptarOferta(oferta.oferta)
            estadoUI = estadoUI.copy(ofertaPorAceptar = null)
        }
    }

    fun onContactarClick(oferta: OfertaConPrestador) {
        estadoUI = estadoUI.copy(ofertaContactada = oferta)
    }

    fun onCerrarContacto() {
        estadoUI = estadoUI.copy(ofertaContactada = null)
    }
}