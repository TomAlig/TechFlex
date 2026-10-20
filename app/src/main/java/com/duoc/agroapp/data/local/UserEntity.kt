package com.duoc.agroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Esta clase sirve para el dao, es la entidad que define los parametros necesarios para el login.
@Entity(tableName = "usuarios_tabla")
data class UserEntity (
    //Se definen los atributos a evaluar, siendo la llave unica el correo.
    @PrimaryKey val email: String,
                val contrasena: String,
                val esUltimoLogeado: Boolean = true
)