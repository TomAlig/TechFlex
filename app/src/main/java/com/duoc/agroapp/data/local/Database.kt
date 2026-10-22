package com.duoc.agroapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room

//Lee los contenidos de UserEntity, y crea una tabla con entities =.
//version se va cambiando cada cambio que se le haga a la db para que se vaya migrando.
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class DatabaseFlex : RoomDatabase() {
    //Se obtiene el dao con la funcion abstracta despues de la anotacion @Database
    abstract fun userDao(): UserDao

    companion object {
        //Volatile permite que toda la app mantenga estos cambios en la sesion.
        @Volatile
        //Al principio esta vacia, no tiene ningun valor.
        private var INSTANCE: DatabaseFlex ? = null

        //Se obtiene la base de datos y sus instancias.
        fun getDatabase(context: Context): DatabaseFlex {
            //levanta las tablas en la conexion, crea el archivo para el celular.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseFlex::class.java,
                    "database_flex" // Nombre del archivo guardado en el celular.
                ).build()
                //la lista guardada se pasa a la variable global INSTANCE. (la conexion)
                INSTANCE = instance
                instance
            }
        }
    }

}