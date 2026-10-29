package com.duoc.agroapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OfertaDao {
    //Guarda una oferta nueva hecha por un prestador.
    @Insert
    suspend fun guardarOferta(oferta: OfertaEntity)

    //Busca si este prestador ya ofertó en esta solicitud, para no dejarlo ofertar dos veces.
    @Query("SELECT * FROM ofertas_tabla WHERE solicitudId = :solicitudId AND emailPrestador = :email LIMIT 1")
    suspend fun buscarOfertaDelPrestador(solicitudId: Int, email: String): OfertaEntity?

    //Todas las ofertas de una solicitud, de la más barata a la más cara.
    //No la usa el Detalle: es para la vista "Ofertas" del productor.
    @Query("SELECT * FROM ofertas_tabla WHERE solicitudId = :solicitudId ORDER BY precioTotal ASC")
    fun obtenerOfertasDeSolicitud(solicitudId: Int): Flow<List<OfertaEntity>>
}