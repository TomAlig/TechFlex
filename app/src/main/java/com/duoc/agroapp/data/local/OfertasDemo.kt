package com.duoc.agroapp.data.demo

import com.duoc.agroapp.model.OfertaEjemplo

//Datos ficticios para demostrar la pantalla de ofertas. No hay base de datos ni red.
object OfertasDemo {

    fun obtenerOfertas(): List<OfertaEjemplo> {
        return listOf(
            OfertaEjemplo(
                id = 1,
                nombrePrestador = "Juanito Pere",
                calificacion = 3.4,
                precioPorHa = 1000,
                descripcion = "Aplicación con dron en frutales y viñas. Atiendo lotes de hasta 30 ha."
            ),
            OfertaEjemplo(
                id = 2,
                nombrePrestador = "Maria Lope",
                calificacion = 4.0,
                precioPorHa = 1200,
                descripcion = "Fumigación líquida y granulada. Cuento con 6 años de experiencia en la zona."
            ),
            OfertaEjemplo(
                id = 3,
                nombrePrestador = "Pedro Roca",
                calificacion = 5.0,
                precioPorHa = 2000,
                descripcion = "Agricultura de precisión con drones. Aplicación diurna y nocturna, disponible de inmediato."
            )
        )
    }
}