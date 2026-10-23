package com.duoc.agroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.agroapp.data.local.DatabaseFlex
import com.duoc.agroapp.repository.LoginRepository
import com.duoc.agroapp.repository.SolicitudRepository
import com.duoc.agroapp.ui.screens.CrearSolicitudScreen
import com.duoc.agroapp.ui.screens.LoginScreen
import com.duoc.agroapp.ui.screens.MisSolicitudesScreen
import com.duoc.agroapp.ui.theme.AgroAppTheme
import com.duoc.agroapp.viewmodel.CrearSolicitudViewModel
import com.duoc.agroapp.viewmodel.LoginViewModel
import com.duoc.agroapp.viewmodel.MisSolicitudesViewModel
import com.duoc.agroapp.viewmodel.SolicitudesCercaViewModel
import com.duoc.agroapp.ui.screens.SolicitudesCercaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Se inicializan los valores de los distintos modulos.
        val database = DatabaseFlex.getDatabase(applicationContext)
        val dao = database.userDao()
        val repository = LoginRepository(dao)
        val viewModel = LoginViewModel(repository)

        val solicitudRepository = SolicitudRepository(database.solicitudDao())
        //Correo temporal: cuando el login pase el usuario, se reemplaza por el real.
        val crearSolicitudViewModel = CrearSolicitudViewModel(solicitudRepository, "productor@demo.cl")
        val misSolicitudesViewModel = MisSolicitudesViewModel(solicitudRepository, "productor@demo.cl")
        val solicitudesCercaViewModel = SolicitudesCercaViewModel(solicitudRepository)

        setContent {
            AgroAppTheme {
                val navController = rememberNavController()
                //Para probar otra pantalla, cambia startDestination: "login", "mis_solicitudes" o "crear_solicitud" o "solicitudes_cerca".
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(viewModel = viewModel) }
                    composable("mis_solicitudes") {
                        MisSolicitudesScreen(
                            viewModel = misSolicitudesViewModel,
                            onNuevaSolicitud = { navController.navigate("crear_solicitud") }
                        )
                    }
                    composable("crear_solicitud") {
                        CrearSolicitudScreen(
                            viewModel = crearSolicitudViewModel,
                            onSolicitudCreada = { navController.popBackStack() },
                            onVolver = { navController.popBackStack() }
                        )
                    }
                    composable("solicitudes_cerca") {
                        SolicitudesCercaScreen(
                            viewModel = solicitudesCercaViewModel,
                            //El detalle es el próximo feature; por ahora tocar la tarjeta no hace nada.
                            onVerDetalle = { }
                        )
                    }
                }
            }
        }
    }
}