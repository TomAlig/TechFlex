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
import com.duoc.agroapp.viewmodel.PerfilViewModel
import com.duoc.agroapp.viewmodel.RegistroViewModel
import com.duoc.agroapp.repository.PerfilRepository
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.duoc.agroapp.repository.NavegacionRepository
import com.duoc.agroapp.viewmodel.NavViewModel
import kotlinx.coroutines.launch

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
        val registroRepository = RegistroRepository(dao)
        val registroViewModel = RegistroViewModel(registroRepository)
        val miPerfilRepository = PerfilRepository(dao)
        val perfilViewModel = PerfilViewModel(miPerfilRepository)
        val navegacionRepository = NavegacionRepository(dao)


        setContent {
            AgroAppTheme {
                //Gestiona la logica de la navegacion.
                val navController = rememberNavController()

                //Se instancia el viewmodel correctamente con factory
                val navViewModel: NavViewModel = viewModel(
                    factory = NavViewModel.NavViewModelFactory(navegacionRepository)
                )
                // deteccion para la pantalla actual.
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route

                val menuItems by navViewModel.menuItems.observeAsState(emptyList())
                Scaffold(
                    bottomBar = {
                        // Lugares en donde no se muestra.
                        val rutasSinBarra = setOf(
                            "login",
                            "registro",
                            "perfil"
                        )
                        val mostrarMenu =
                            rutaActual !in rutasSinBarra &&
                                    menuItems.isNotEmpty()
                        if (mostrarMenu) {
                            NavigationBar {
                                menuItems.forEach { item ->
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                imageVector = item.icono,
                                                contentDescription = item.titulo
                                            )
                                        },
                                        label = {
                                            Text(text = item.titulo)
                                        },
                                        selected = rutaActual == item.ruta,
                                        onClick = {
                                            if (rutaActual != item.ruta) {
                                                navController.navigate(item.ruta) {
                                                    launchSingleTop = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = { //Vaw ingresando tras un login exitoso.
                                    //Se toma el dato del usuario logueado de perfilrepo.
                                    val correo = PerfilViewModel.emailUsuarioLogueado

                                    if (correo != "") {
                                        lifecycleScope.launch {
                                            val usuarioActual = dao.buscarPorEmail(correo)

                                            val tipoUsuario = usuarioActual?.tipoUsuario

                                            if (tipoUsuario != null) {
                                                navViewModel.cargarMenu(tipoUsuario)
                                                val destinoInicial = when (tipoUsuario) {
                                                    "Productor" -> "mis_solicitudes"
                                                    "Prestador" -> "solicitudes_cerca"
                                                    else -> "perfil"
                                                }
                                                navController.navigate(destinoInicial) {
                                                    //saca login para que no aparezca al apretar hacia atras en el telefono.
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                        }
                                    }
                                },
                                onNavigateToRegister = { //nav a registro por el boton correspondiente.
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
                                viewmodel = perfilViewModel,
                                onCerrarSesion = {
                                    viewModel.cerrarSesion {
                                        navController.navigate("login") {
                                            popUpTo("perfil") { inclusive = true }
                                        }
                                    }
                                },
                                onVolver = {
                                    //Al hacer click en la flecha para atras, se devuelve una pagina del monton.
                                    navController.popBackStack()
                                }

                            )
                        }
                    }
                }
            }
        }
    }
}