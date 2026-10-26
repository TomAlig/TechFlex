package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.model.RegistroEstadoUI
import com.duoc.agroapp.repository.RegistroRepository
import com.duoc.agroapp.validation.RegistroValidator
import kotlinx.coroutines.launch

//Maneja el formulario de registro: guarda lo que escribe el usuario, valida y registra.
class RegistroViewModel(
    private val repository: RegistroRepository
) : ViewModel() {

    var estadoUI by mutableStateOf(RegistroEstadoUI())
        private set

    //Al modificar un campo se limpia su error anterior.
    fun onRutChange(valor: String) {
        estadoUI = estadoUI.copy(rut = valor, errorRut = null, errorGeneral = null)
    }

    fun onNombreChange(valor: String) {
        estadoUI = estadoUI.copy(nombre = valor, errorNombre = null)
    }

    fun onCorreoChange(valor: String) {
        estadoUI = estadoUI.copy(correo = valor, errorCorreo = null, errorGeneral = null)
    }

    fun onContrasenaChange(valor: String) {
        estadoUI = estadoUI.copy(contrasena = valor, errorContrasena = null)
    }

    fun onRepetirContrasenaChange(valor: String) {
        estadoUI = estadoUI.copy(repetirContrasena = valor, errorRepetir = null)
    }

    fun onTelefonoChange(valor: String) {
        estadoUI = estadoUI.copy(telefono = valor, errorTelefono = null)
    }

    fun onSuscritoChange(valor: Boolean) {
        estadoUI = estadoUI.copy(suscritoNovedades = valor)
    }

    fun onTipoUsuarioChange(valor: String) {
        estadoUI = estadoUI.copy(tipoUsuario = valor, errorTipoUsuario = null)
    }

    //Valida todos los campos a la vez, para mostrar todos los errores juntos.
    fun onRegistrarClick() {
        val errorRut = RegistroValidator.validarRut(estadoUI.rut)
        val errorNombre = RegistroValidator.validarNombre(estadoUI.nombre)
        val errorCorreo = RegistroValidator.validarCorreo(estadoUI.correo)
        val errorContrasena = RegistroValidator.validarContrasena(estadoUI.contrasena)
        val errorRepetir = RegistroValidator.validarRepetirContrasena(estadoUI.contrasena, estadoUI.repetirContrasena)
        val errorTelefono = RegistroValidator.validarTelefono(estadoUI.telefono)
        val errorTipo = RegistroValidator.validarTipoUsuario(estadoUI.tipoUsuario)

        estadoUI = estadoUI.copy(
            errorRut = errorRut,
            errorNombre = errorNombre,
            errorCorreo = errorCorreo,
            errorContrasena = errorContrasena,
            errorRepetir = errorRepetir,
            errorTelefono = errorTelefono,
            errorTipoUsuario = errorTipo
        )

        //Si algún campo tiene error, no se registra.
        val formularioValido = listOf(
            errorRut, errorNombre, errorCorreo, errorContrasena, errorRepetir, errorTelefono, errorTipo
        ).all { it == null }
        if (!formularioValido) return

        registrar()
    }

    //Guarda el usuario en Room. Los datos ya fueron validados.
    private fun registrar() {
        viewModelScope.launch {
            estadoUI = estadoUI.copy(guardando = true)

            val error = repository.registrar(
                UserEntity(
                    //El correo en minúsculas y el RUT limpio, para que siempre se guarden igual.
                    email = estadoUI.correo.trim().lowercase(),
                    contrasena = estadoUI.contrasena,
                    rut = RegistroValidator.limpiarRut(estadoUI.rut),
                    nombre = estadoUI.nombre.trim(),
                    telefono = estadoUI.telefono.replace(" ", "").removePrefix("+56"),
                    tipoUsuario = estadoUI.tipoUsuario ?: "",
                    suscritoNovedades = estadoUI.suscritoNovedades == true
                )
            )

            //null = se registró bien. Si trae texto (correo o RUT repetido), se muestra en pantalla.
            estadoUI = estadoUI.copy(
                guardando = false,
                errorGeneral = error,
                registroExitoso = error == null
            )
        }
    }

    //Se llama luego de navegar, deja el formulario limpio.
    fun onNavegacionConsumida() {
        estadoUI = RegistroEstadoUI()
    }
}