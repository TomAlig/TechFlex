package com.duoc.agroapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.duoc.agroapp.model.LoginEstadoUI
import com.duoc.agroapp.repository.LoginRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.model.LoginResultado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Se recolecta la logica de negocio del repository y se crean funciones para la ui, validaciones y verificaciones en los inputs.
class LoginViewModel(private val repository: LoginRepository): ViewModel() {
    var estadoUI by mutableStateOf(LoginEstadoUI())
        private set

    init {
        viewModelScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                repository.obtenerUltimoUsuario()
            }
            if (usuario != null) {
                estadoUI = estadoUI.copy(
                    loginResultado = LoginResultado(exito = true)
                )
            }
        }
    }

    fun cambiarMantener(valor: Boolean) {
        estadoUI = estadoUI.copy(mantener = valor)
    }

    //Actualiza input email.
    fun onEmailChange(nuevoEmail: String) {
        estadoUI = estadoUI.copy(email = nuevoEmail)
    }

    //Actualiza input contraseña.
    fun onPasswordChange(nuevaPass: String) {
        estadoUI = estadoUI.copy(password = nuevaPass)
    }

    //Verifica que los campos no esten vacios y que el campo de email tenga un "@".
    //guardarDatos se utiliza para darle valor real al checkbox de recuerdame
    fun iniciarSesion() {
        if (estadoUI.email.isBlank() || estadoUI.password.isBlank()) {
            estadoUI = estadoUI.copy(
                loginResultado = LoginResultado(
                    exito = false,
                    errorMsg = "Completa todos los campos"
                )
            )
            return
        }

        //Verificacion de correo inicial.
        if (!estadoUI.email.contains("@")) {
            estadoUI = estadoUI.copy(
                loginResultado = LoginResultado(
                    exito = false,
                    errorMsg = "El correo electrónico debe contener un '@'"
                )
            )
            return
        }

        //Si atraviesa los ifs que retornan:
        viewModelScope.launch {
            estadoUI = estadoUI.copy(cargando = true)

            // Guardamos en Room usando repository
            repository.simularLoginExitoso(estadoUI.email, estadoUI.password, estadoUI.mantener)

            estadoUI = estadoUI.copy(
                cargando = false,
                loginResultado = LoginResultado(exito = true)
            )
        }
    }

    //Funcion para volver al login desde perfil
    //Contiene una corrutina para el borrado de Room
    suspend fun cerrarSesion() {
        //Dispatchers.IO pone a los trabajos en segundo plano a ejecutar.
        withContext(Dispatchers.IO) {
            repository.cerrarSesion() // Borra Room
        }

        // Reinicia el estado de la interfaz
        estadoUI = LoginEstadoUI()

        }
    }
