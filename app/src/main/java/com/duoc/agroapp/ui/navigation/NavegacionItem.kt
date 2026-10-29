package com.duoc.agroapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

//Aqui definimos el modelo po item de navegación, para poder definir su apariencia y su routeo.
data class NavegacionItem(
    val titulo: String,
    val ruta: String,
    //ImageVector maneja los iconos vectoriales que se van a usar en la navegación.
    val icono: ImageVector
)


