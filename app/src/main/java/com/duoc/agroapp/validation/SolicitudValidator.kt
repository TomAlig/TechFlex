package com.duoc.agroapp.validation

//Reglas de validación del formulario. Retornan el mensaje de error, o null si el campo es válido.
object SolicitudValidator {

    fun validarTipoProducto(tipoProducto: String): String? {
        return if (tipoProducto.isBlank()) "Selecciona un tipo de producto" else null
    }

    fun validarSuperficie(superficie: String): String? {
        if (superficie.isBlank()) return "Ingresa la superficie en hectáreas"
        val numero = superficie.toDoubleOrNull() ?: return "Ingresa un número válido"
        if (numero <= 0) return "La superficie debe ser mayor a 0"
        if (numero > 10000) return "La superficie máxima es 10.000 ha"
        return null
    }

    //Regla de negocio: si marca "Otros" como obstáculo, debe describirlo en los comentarios.
    fun validarComentarios(comentarios: String, marcoOtros: Boolean): String? {
        if (marcoOtros && comentarios.isBlank()) return "Describe el obstáculo marcado como 'Otros'"
        if (comentarios.length > 200) return "Máximo 200 caracteres"
        return null
    }
}