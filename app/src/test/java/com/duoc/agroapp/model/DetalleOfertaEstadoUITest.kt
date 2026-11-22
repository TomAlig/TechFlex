package com.duoc.agroapp.model

import com.duoc.agroapp.data.local.SolicitudEntity
import org.junit.Assert
import org.junit.Test

class DetalleOfertaEstadoUITest {


    private fun solicitudConSuperficie(superficieHa: Double) = SolicitudEntity(
        id = 1,
        emailUsuario = "productor@correo.cl",
        tipoProducto = "Líquida",
        superficieHa = superficieHa,
        tieneAgua = true,
        aplicacion = "Diurna",
        obstaculos = "",
        comentarios = ""
    )

    @Test
    fun `sin precio el total es nulo`() {
        println("INICIO: comprobar el total cuando no hay precio.")
        println("PREPARACION: hay una solicitud de 10 ha y el precio esta vacio.")
        val estado = DetalleOfertaEstadoUI(solicitud = solicitudConSuperficie(10.0), precioTexto = "")

        println("ACCION: leer el precio total.")
        val resultado = estado.precioTotal

        println("COMPROBACION: el total debe ser nulo.")
        Assert.assertNull(resultado)
        println("CORRECTO: no se calculo un total sin precio.")
    }

    @Test
    fun `el total es precio por hectarea por superficie`() {
        println("INICIO: comprobar el calculo del total.")
        println("PREPARACION: la solicitud es de 10.5 ha y el precio es 5000 por ha.")
        val estado = DetalleOfertaEstadoUI(solicitud = solicitudConSuperficie(10.5), precioTexto = "5000")

        println("ACCION: leer el precio total.")
        val resultado = estado.precioTotal

        println("COMPROBACION: el total debe ser 52500.")
        Assert.assertEquals(52_500L, resultado)
        println("CORRECTO: el total es el esperado.")
    }

    @Test
    fun `el total se redondea al entero mas cercano`() {
        println("INICIO: comprobar el redondeo del total.")
        println("PREPARACION: la solicitud es de 1.5 ha y el precio es 3333 por ha.")
        val estado = DetalleOfertaEstadoUI(solicitud = solicitudConSuperficie(1.5), precioTexto = "3333")

        println("ACCION: leer el precio total.")
        val resultado = estado.precioTotal

        println("COMPROBACION: 4999.5 debe redondearse a 5000.")
        Assert.assertEquals(5_000L, resultado)
        println("CORRECTO: el total se redondeo.")
    }
}