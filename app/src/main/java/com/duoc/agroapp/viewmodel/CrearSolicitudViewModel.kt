package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.SolicitudEntity
import com.duoc.agroapp.model.CrearSolicitudEstadoUI
import com.duoc.agroapp.model.OBSTACULO_OTROS
import com.duoc.agroapp.repository.SolicitudRepository
import com.duoc.agroapp.validation.SolicitudValidator
import kotlinx.coroutines.launch

//Maneja el estado del formulario, llama a las validaciones y guarda la solicitud.
class CrearSolicitudViewModel(
    private val repository: SolicitudRepository,
    private val emailUsuario: String
) : ViewModel() {

    var estadoUI by mutableStateOf(CrearSolicitudEstadoUI())
        private set

    //Al modificar un campo se limpia su error anterior.
    fun onTipoProductoChange(valor: String) {
        estadoUI = estadoUI.copy(tipoProducto = valor, errorTipoProducto = null)
    }

    //Solo se aceptan dígitos y punto decimal.
    fun onSuperficieChange(valor: String) {
        if (valor.all { it.isDigit() || it == '.' }) {
            estadoUI = estadoUI.copy(superficie = valor, errorSuperficie = null)
        }
    }

    fun onTieneAguaChange(valor: Boolean) {
        estadoUI = estadoUI.copy(tieneAgua = valor)
    }

    fun onAplicacionChange(valor: String) {
        estadoUI = estadoUI.copy(aplicacion = valor)
    }

    fun onObstaculoChange(obstaculo: String, marcado: Boolean) {
        val nuevos = if (marcado) estadoUI.obstaculos + obstaculo else estadoUI.obstaculos - obstaculo
        estadoUI = estadoUI.copy(obstaculos = nuevos, errorComentarios = null)
    }

    fun onComentariosChange(valor: String) {
        estadoUI = estadoUI.copy(comentarios = valor, errorComentarios = null)
    }

    //Guarda la ruta de la foto tomada con la cámara.
    fun onFotoCapturada(uri: String) {
        estadoUI = estadoUI.copy(fotoUri = uri, errorFoto = null)
    }

    fun onPermisoCamaraDenegado() {
        estadoUI = estadoUI.copy(errorFoto = "Sin permiso de cámara no se puede tomar la foto")
    }

    //Valida todos los campos; si todo está bien, abre el modal de confirmación.
    fun onEnviarClick() {
        val errorTipo = SolicitudValidator.validarTipoProducto(estadoUI.tipoProducto)
        val errorSuperficie = SolicitudValidator.validarSuperficie(estadoUI.superficie)
        val errorComentarios = SolicitudValidator.validarComentarios(
            estadoUI.comentarios,
            estadoUI.obstaculos.contains(OBSTACULO_OTROS)
        )
        val formularioValido = errorTipo == null && errorSuperficie == null && errorComentarios == null

        estadoUI = estadoUI.copy(
            errorTipoProducto = errorTipo,
            errorSuperficie = errorSuperficie,
            errorComentarios = errorComentarios,
            mostrarDialogoConfirmacion = formularioValido
        )
    }

    fun onCancelarConfirmacion() {
        estadoUI = estadoUI.copy(mostrarDialogoConfirmacion = false)
    }

    //Guarda la solicitud en Room. La superficie ya fue validada, por eso toDouble() es seguro.
    fun onConfirmarEnvio() {
        viewModelScope.launch {
            estadoUI = estadoUI.copy(mostrarDialogoConfirmacion = false, guardando = true)
            repository.crearSolicitud(
                SolicitudEntity(
                    emailUsuario = emailUsuario,
                    tipoProducto = estadoUI.tipoProducto,
                    superficieHa = estadoUI.superficie.toDouble(),
                    tieneAgua = estadoUI.tieneAgua,
                    aplicacion = estadoUI.aplicacion,
                    obstaculos = estadoUI.obstaculos.joinToString(","),
                    comentarios = estadoUI.comentarios.trim(),
                    fotoUri = estadoUI.fotoUri
                )
            )
            estadoUI = estadoUI.copy(guardando = false, guardadoExitoso = true)
        }
    }

    //Se llama luego de navegar, deja el formulario limpio.
    fun onNavegacionConsumida() {
        estadoUI = CrearSolicitudEstadoUI()
    }
}