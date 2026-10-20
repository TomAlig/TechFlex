package com.duoc.agroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.agroapp.data.local.DatabaseFlex
import com.duoc.agroapp.repository.LoginRepository
import com.duoc.agroapp.ui.theme.AgroAppTheme
import com.duoc.agroapp.viewmodel.LoginViewModel
import com.duoc.agroapp.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Se inicializan los valores de los distintos modulos.
        val database = DatabaseFlex.getDatabase(applicationContext)
        val dao = database.userDao()
        val repository = LoginRepository(dao)
        val viewModel = LoginViewModel(repository)
        setContent {
            AgroAppTheme {
                //Muestra la pantalla.
                    LoginScreen(viewModel = viewModel)
                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgroAppTheme {
        Greeting("Android")
    }
}