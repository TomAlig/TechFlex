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
import com.duoc.agroapp.repository.RegistroRepository
import com.duoc.agroapp.ui.screens.PerfilUsuarioScreen
import com.duoc.agroapp.ui.screens.RegistroScreen
import com.duoc.agroapp.viewmodel.RegistroViewModel

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
        val crearSolicitudViewModel =
            CrearSolicitudViewModel(solicitudRepository, "productor@demo.cl")
        val misSolicitudesViewModel =
            MisSolicitudesViewModel(solicitudRepository, "productor@demo.cl")
        val solicitudesCercaViewModel = SolicitudesCercaViewModel(solicitudRepository)
        val registroRepository = RegistroRepository(dao)
        val registroViewModel = RegistroViewModel(registroRepository)

        setContent {
            AgroAppTheme {
                val navController = rememberNavController()
                //Para probar otra pantalla, cambia startDestination: "login", "mis_solicitudes" o "crear_solicitud" o "solicitudes_cerca".
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                                navController.navigate("perfil") {
                                    //saca login para que no aparezca al apretar hacia atras en el telefono.
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("registro")
                            }
                        )
                    }

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
                    composable("registro") {
                        RegistroScreen(
                            viewModel = registroViewModel,
                            onRegistroExitoso = {
                                //Después de registrarse va al login, y el registro sale del historial
                                //para que el botón "atrás" no vuelva al formulario.
                                navController.navigate("login") {
                                    popUpTo("registro") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("perfil") {

                        PerfilUsuarioScreen(
                            onCerrarSesion = {
                                //Vacia room
                                viewModel.cerrarSesion()
                                navController.navigate("login") {
                                    popUpTo("perfil") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}