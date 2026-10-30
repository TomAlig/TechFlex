package com.duoc.agroapp.repository
import com.duoc.agroapp.data.local.UserDao
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import com.duoc.agroapp.ui.navigation.NavegacionItem

class NavegacionRepository (private val userDao: UserDao) {
    //Maneja la logica de routeo a partir del tipo de usuario en el item de navegacion.
    fun obtenerMenu(tipoUsuario: String): List<NavegacionItem> {
        //Array de opciones para cada user + perfil global abajo.
        val items = when (tipoUsuario) {
            "Productor" -> listOf(
                NavegacionItem(
                    titulo = "Nueva Solicitud",
                    ruta =  "crear_solicitud",
                    icono = Icons.Default.AddCircle
                ),
                NavegacionItem(
                    titulo = "Mis Solicitudes",
                    ruta = "mis_solicitudes",
                    icono = Icons.Default.List
                )
            )

            "Prestador" -> listOf(
                NavegacionItem(
                    titulo = "Buscar Cerca",
                    ruta = "solicitudes_cerca",
                    icono = Icons.Default.LocationOn
                )
            )
            //El admin ve su vista de usuarios (y Mi Perfil, que se suma abajo para todos).
            "Administrador" -> listOf(
                NavegacionItem(
                    titulo = "Usuarios",
                    ruta = "admin",
                    icono = Icons.Default.Settings
                )
            )

            else -> emptyList()
        }
        //devuelve los items por tipo de user, y le suma al retorno el perfil para ambos tipos.
        return items + NavegacionItem(
            titulo = "Mi Perfil",
            ruta = "perfil",
            icono = Icons.Default.Person
        )
    }
}