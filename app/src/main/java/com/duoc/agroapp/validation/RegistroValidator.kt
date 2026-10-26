package com.duoc.agroapp.validation

//Reglas de validación del registro. Retornan el mensaje de error, o null si el campo es válido.
object RegistroValidator {

    private val REGEX_NOMBRE = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$")
    private val REGEX_CORREO = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")
    private val REGEX_CELULAR = Regex("^9\\d{8}$")   //celular chileno: 9 dígitos que parten con 9

    private val REGEX_RUT = Regex("^\\d{7,8}[0-9K]$")   //7 u 8 números + dígito verificador (número o K)

    fun validarRut(rut: String): String? {
        val limpio = limpiarRut(rut)
        if (limpio.isEmpty()) return "Ingresa tu RUT"
        if (!REGEX_RUT.matches(limpio)) return "Formato de RUT inválido (ej: 12.345.678-9)"
        return null
    }

    //Quita puntos, guion y espacios, y deja la K en mayúscula: "12.345.678-k" -> "12345678K"
    fun limpiarRut(rut: String): String =
        rut.replace(".", "").replace("-", "").trim().uppercase()


    fun validarNombre(nombre: String): String? {
        if (nombre.isBlank()) return "Ingresa tu nombre"
        if (nombre.trim().length < 2) return "El nombre es muy corto"
        if (!REGEX_NOMBRE.matches(nombre.trim())) return "El nombre solo puede tener letras"
        return null
    }

    fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return "Ingresa tu correo"
        if (!REGEX_CORREO.matches(correo.trim())) return "Correo inválido"
        return null
    }

    fun validarContrasena(contrasena: String): String? {
        if (contrasena.isEmpty()) return "Ingresa una contraseña"
        if (contrasena.length < 8) return "Debe tener al menos 8 caracteres"
        if (!contrasena.any { it.isDigit() }) return "Debe tener al menos un número"
        return null
    }

    fun validarRepetirContrasena(contrasena: String, repetir: String): String? {
        if (repetir.isEmpty()) return "Repite la contraseña"
        if (repetir != contrasena) return "Las contraseñas no coinciden"
        return null
    }

    fun validarTelefono(telefono: String): String? {
        //Se aceptan "+56" y espacios; se quitan antes de validar.
        val limpio = telefono.replace(" ", "").removePrefix("+56")
        if (limpio.isEmpty()) return "Ingresa tu teléfono"
        if (!REGEX_CELULAR.matches(limpio)) return "Ingresa un celular de 9 dígitos (ej: 912345678)"
        return null
    }

    fun validarTipoUsuario(tipoUsuario: String?): String? {
        return if (tipoUsuario == null) "Elige si eres Prestador o Productor" else null
    }
}