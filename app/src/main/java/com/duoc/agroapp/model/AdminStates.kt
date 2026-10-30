package com.duoc.agroapp.model

import com.duoc.agroapp.data.local.UserEntity

//Estado de la vista del administrador.
data class AdminEstadoUI(
    val usuarios: List<UserEntity> = emptyList(),
    //Empieza en true porque al abrir la pantalla todavía no llegan los datos.
    val cargando: Boolean = true,
    //Usuario que el admin quiere eliminar. Si no es null, se muestra el modal de confirmación.
    val usuarioAEliminar: UserEntity? = null
)