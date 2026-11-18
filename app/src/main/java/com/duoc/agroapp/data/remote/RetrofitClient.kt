package com.duoc.agroapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // La base_url es solo una, porque en el apigroup estan todos los endpoints.
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:ZaIqlMn1/"

    //Se conecta a la base de xano, y construye apiservice por separado para las peticiones.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //val usuarioApi: UsuarioApiService by lazy {
    //    retrofit.create(UsuarioApiService::class.java)
    //}

    val solicitudApi: SolicitudApiService by lazy {
        retrofit.create(SolicitudApiService::class.java)
    }

    //val ofertaApi: OfertaApiService by lazy {
    //    retrofit.create(OfertaApiService::class.java)
    //}
}

    /*Se agregan bloques para distintos endpoints en formato
    val X-Api: X-ApiService by lazy...
    Faltan esos 2.
    */