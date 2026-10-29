package com.duoc.agroapp.validation

//Reglas de validación de la oferta. Retornan el mensaje de error, o null si es válido.
object OfertaValidator {

    //Tope razonable por hectárea, para evitar errores de tipeo (ej: un cero de más).
    private const val PRECIO_MAXIMO_POR_HA = 10_000_000L

    fun validarPrecio(precioTexto: String): String? {
        if (precioTexto.isBlank()) return "Ingresa un precio por hectárea"
        val precio = precioTexto.toLongOrNull() ?: return "Ingresa solo números"
        if (precio <= 0) return "El precio debe ser mayor a 0"
        if (precio > PRECIO_MAXIMO_POR_HA) return "El precio máximo es $10.000.000 por ha"
        return null
    }
}