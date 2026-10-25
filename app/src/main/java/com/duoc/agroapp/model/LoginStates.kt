package com.duoc.agroapp.model

//Resultado del ingreso - (No en uso)
data class LoginResultado(
    val exito: Boolean,
    // ?= Es nullable.
    val errorMsg: String ? = null
)

//Estado del formulario.
data class LoginEstadoUI(
    val email: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val loginResultado: LoginResultado ? = null,

    //Propiedad agregada para manejar el salto del login al dashboard si "Recordarme" esta marcado.
    val mantener: Boolean = false,

    //Propiedad para controlar el paso a dashboard
    val irAlDashboard: Boolean = false
)
