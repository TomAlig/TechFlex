package com.duoc.agroapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Se define como Dao
@Dao
interface UserDao {
    //Inserta (guarda) datos, si hay conflicto se reemplazan por los nuevos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: UserEntity)

    //esUltimoLogeado es un boolean, si hay alguien recordado es un 1, si no, un 0 (true/false)
    @Query("SELECT * FROM usuarios_tabla WHERE esUltimoLogeado = 1 LIMIT 1")
    fun obtenerUltimoUsuario(): UserEntity? //Flow vigila el cambio y cambia las reglas segun corresponda inmendiatamente.

    //Si esUltimoLogeado no está activado, es decir, no hay nadie, se borra el llenado de campos.
    @Query("UPDATE usuarios_tabla SET esUltimoLogeado = 0")
    suspend fun desmarcarUltimosUsuarios()

    //Registra un usuario nuevo. Sin REPLACE: si el correo ya existe, Room no lo sobrescribe.
    @Insert
    suspend fun registrarUsuario(usuario: UserEntity)

    //Busca un usuario registrado por su correo, lo usa el registro y después el login.
    @Query("SELECT * FROM usuarios_tabla WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): UserEntity?

    //Busca un usuario por RUT, para no permitir dos cuentas con el mismo RUT.
    @Query("SELECT * FROM usuarios_tabla WHERE rut = :rut LIMIT 1")
    suspend fun buscarPorRut(rut: String): UserEntity?

    //Marca a al usuario actual como la sesion recordada.
    @Query("UPDATE usuarios_tabla SET esUltimoLogeado = 1 WHERE email = :email")
    suspend fun marcarComoLogeado(email: String)

    ///(Usado por el perfil) //Query para lectura y actualizacion de datos con LiveData
    @Query("SELECT * FROM usuarios_tabla WHERE esUltimoLogeado = 1 LIMIT 1")
    fun obtenerUltimoUsuarioLiveData(): LiveData<UserEntity?>
    @Update
    suspend fun actualizarUsuario(usuario: UserEntity)

    //Query que verifica el usuario en cualquier contexto, sin importar si está recordado a través de su PK.
    @Query("SELECT * FROM usuarios_tabla WHERE email = :email LIMIT 1")
    fun obtenerUsuarioPorEmailLiveData(email: String): LiveData<UserEntity?>

    //Todos los usuarios menos el admin, ordenados por nombre. Lo usa la vista del administrador.
    //Es Flow: cuando se elimina un usuario, la lista se actualiza sola.
    @Query("SELECT * FROM usuarios_tabla WHERE tipoUsuario != 'Administrador' ORDER BY nombre ASC")
    fun obtenerUsuariosParaAdmin(): Flow<List<UserEntity>>

    //Elimina un usuario de la tabla.
    @Delete
    suspend fun eliminarUsuario(usuario: UserEntity)
}
