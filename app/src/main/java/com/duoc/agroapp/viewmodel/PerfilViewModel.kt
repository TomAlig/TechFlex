package com.duoc.agroapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agroapp.data.local.UserEntity
import com.duoc.agroapp.repository.PerfilRepository
import kotlinx.coroutines.launch

class PerfilViewModel(private val repository: PerfilRepository) : ViewModel() {
    companion object {
        // La RAM recuerda el correo mientras la app esta abierta
        var emailUsuarioLogueado: String = ""
    }
    // usuario se guarda como livedata desde el repositorio.
    //el get se usa porque esto permite que los datos en db se muestren actualizados constantemente en el perfil.
    val usuario: LiveData<UserEntity?>  get() = repository.obtenerUsuarioPorEmail(emailUsuarioLogueado)

    // 2. conecta la actualizacion al repositorio para que la ejecucion desde ui lo maneje.
    fun editarDatosUsuario(usuarioActualizado: UserEntity) {
        viewModelScope.launch {
            repository.actualizarUsuario(usuarioActualizado)
        }
    }
}