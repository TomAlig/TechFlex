package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.OfertaEntity
import com.duoc.agroapp.model.DetalleOfertaEstadoUI
import com.duoc.agroapp.repository.OfertaRepository
import com.duoc.agroapp.validation.OfertaValidator
import kotlinx.coroutines.launch

//Carga la solicitud elegida, maneja el precio y envía la oferta del prestador.
class DetalleOfertaViewModel(
    private val repository: OfertaRepository
) : ViewModel() {

    var estadoUI by mutableStateOf(DetalleOfertaEstadoUI())
        private set

    //Se llama cada vez que se abre el Detalle, con el id de la solicitud tocada.
    fun cargar(solicitudId: Int) {
        //Se limpia lo de la solicitud anterior (precio, errores).
        estadoUI = DetalleOfertaEstadoUI()

        viewModelScope.launch {
            //Correo del prestador que inició sesión (lo guarda el login de Tom).
            val email = PerfilViewModel.emailUsuarioLogueado
            val solicitud = repository.obtenerSolicitud(solicitudId)
            val yaOferto = repository.yaOferto(solicitudId, email)

            estadoUI = estadoUI.copy(solicitud = solicitud, ofertaEnviada = yaOferto, cargando = false)
        }
    }

    //Solo se aceptan números, y máximo 9 dígitos.
    fun onPrecioChange(valor: String) {
        if (valor.all { it.isDigit() } && valor.length <= 9) {
            estadoUI = estadoUI.copy(precioTexto = valor, errorPrecio = null, errorGeneral = null)
        }
    }

    fun onOfertarClick() {
        val solicitud = estadoUI.solicitud ?: return

        //Primero se valida el precio; si tiene error, se muestra y no se envía.
        val errorPrecio = OfertaValidator.validarPrecio(estadoUI.precioTexto)
        if (errorPrecio != null) {
            estadoUI = estadoUI.copy(errorPrecio = errorPrecio)
            return
        }

        viewModelScope.launch {
            estadoUI = estadoUI.copy(enviando = true)

            val oferta = OfertaEntity(
                solicitudId = solicitud.id,
                emailPrestador = PerfilViewModel.emailUsuarioLogueado,
                precioPorHa = estadoUI.precioTexto.toLong(),
                precioTotal = estadoUI.precioTotal ?: 0
            )
            val error = repository.enviarOferta(oferta)

            if (error == null) {
                //Se guardó bien.
                estadoUI = estadoUI.copy(enviando = false, ofertaEnviada = true)
            } else {
                //Ej: "Ya enviaste una oferta para esta solicitud".
                estadoUI = estadoUI.copy(enviando = false, errorGeneral = error)
            }
        }
    }
}