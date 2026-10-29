package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.agroapp.data.demo.OfertasDemo
import com.duoc.agroapp.model.OfertaEjemplo
import com.duoc.agroapp.model.OfertasEstadoUI

//Maneja favoritos, aceptación y contacto de ofertas. Es una simulación: nada se guarda.
class OfertasViewModel : ViewModel() {

    var estadoUI by mutableStateOf(OfertasEstadoUI())
        private set

    init {
        //Como en el diseño, el prestador de 5,0 parte marcado con la estrella.
        estadoUI = estadoUI.copy(ofertas = OfertasDemo.obtenerOfertas(), favoritosIds = setOf(3))
    }

    //Marca o desmarca la estrella de un prestador.
    fun onFavoritoClick(idOferta: Int) {
        val nuevos = if (estadoUI.favoritosIds.contains(idOferta)) {
            estadoUI.favoritosIds - idOferta
        } else {
            estadoUI.favoritosIds + idOferta
        }
        estadoUI = estadoUI.copy(favoritosIds = nuevos)
    }

    //Regla de negocio: solo se puede aceptar una oferta; después las demás quedan bloqueadas.
    fun onAceptarClick(oferta: OfertaEjemplo) {
        if (estadoUI.ofertaAceptadaId == null) {
            estadoUI = estadoUI.copy(ofertaPorAceptar = oferta)
        }
    }

    fun onCancelarAceptar() {
        estadoUI = estadoUI.copy(ofertaPorAceptar = null)
    }

    fun onConfirmarAceptar() {
        val oferta = estadoUI.ofertaPorAceptar ?: return
        estadoUI = estadoUI.copy(ofertaAceptadaId = oferta.id, ofertaPorAceptar = null)
    }

    //Simula el envío de un mensaje al prestador.
    fun onContactarClick(oferta: OfertaEjemplo) {
        estadoUI = estadoUI.copy(ofertaContactada = oferta)
    }

    fun onCerrarContacto() {
        estadoUI = estadoUI.copy(ofertaContactada = null)
    }
}