package com.duoc.agroapp.model

//Tipos de usuario. Es el texto que se guarda en UserEntity.tipoUsuario.
const val TIPO_PRESTADOR = "Prestador"
const val TIPO_PRODUCTOR = "Productor"

//Estado completo del formulario "Registro".
data class RegistroEstadoUI(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val repetirContrasena: String = "",
    val telefono: String = "",
    //null = todavía no elige; en el mockup ninguna opción viene marcada.
    val suscritoNovedades: Boolean? = null,
    val tipoUsuario: String? = null,

    //Un error por campo (null = campo válido).
    val errorRut: String? = null,
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorRepetir: String? = null,
    val errorTelefono: String? = null,
    val errorTipoUsuario: String? = null,
    //Error que no pertenece a un campo, ej: "Ya existe una cuenta con este correo".
    val errorGeneral: String? = null,

    val guardando: Boolean = false,
    val registroExitoso: Boolean = false
)