package com.duoc.agroapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duoc.agroapp.repository.NavegacionRepository
import com.duoc.agroapp.ui.navigation.NavegacionItem

//Gestiona el estado de las opciones disponibles por usuario.
//Proporciona la lista para el interfaz
class NavViewModel(
    private val navegacionRepository: NavegacionRepository
) : ViewModel() {

    //lista interna para opciones, esto almacena las opciones cargadas para el usuario.
    private val _menuItems =
        MutableLiveData<List<NavegacionItem>>(emptyList())

    //Expone la lista con LiveData para que la interfaz (resto de componentes), puedan observar sus cambios.
    val menuItems: LiveData<List<NavegacionItem>> = _menuItems

    //Obtiene las opciones correspondientes al rol, y actualiza la lista observable. cargamenu -> _menuItems -> menuItems.
    fun cargarMenu(tipoUsuario: String) {
        _menuItems.value =
            navegacionRepository.obtenerMenu(tipoUsuario)
    }

//Parametriza la creacion del viewmodel, es un manual para su implementacion en mainactivity.
class NavViewModelFactory(
    private val repository: NavegacionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    //T Representa un dato de tipo viewmodel
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        //Si corresponde, a el viewmodel de navegacion, retorna eso, si no, arroja excepcion.
        if (modelClass.isAssignableFrom(NavViewModel::class.java)) {
            return NavViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Clase ViewModel desconocida"
            )
        }
    }
}
