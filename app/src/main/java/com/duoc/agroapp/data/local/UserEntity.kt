package com.duoc.agroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Tabla de usuarios registrados. La llave única es el correo.
@Entity(tableName = "usuarios_tabla")
data class UserEntity(
    @PrimaryKey val email: String,
    val contrasena: String,
    //Datos del registro. Tienen valor por defecto para que el código del login siga compilando.
    val rut: String = "",
    val nombre: String = "",
    val telefono: String = "",
    val tipoUsuario: String = "",          //"Prestador" o "Productor"
    val suscritoNovedades: Boolean = false,
    //Marca al usuario que tiene la sesión recordada (función "Recuérdame" de Tom).
    val esUltimoLogeado: Boolean = false
)