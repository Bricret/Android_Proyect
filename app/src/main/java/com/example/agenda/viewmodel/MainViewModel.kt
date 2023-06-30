package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.config.PersonalApp.Companion.db
import com.example.agenda.models.Personal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    val personalList = MutableLiveData<List<Personal>?>()  /* se le agrego ese "?" no tengo idea porque pero lo soluciono*/
    var parametroBusqueda = MutableLiveData<String>()

    fun Iniciar(){
        viewModelScope.launch {
            personalList.value = withContext(Dispatchers.IO){

                db.personalDao().getAll()
            }
        }
    }

    fun buscarPersonal() {
        viewModelScope.launch {
            personalList.value = withContext(Dispatchers.IO){

                db.personalDao().getByName(parametroBusqueda.value!!)
            }
        }
    }
}