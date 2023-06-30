package com.example.agenda.ui

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agenda.MainActivity
import com.example.agenda.R
import com.example.agenda.config.Constantes
import com.example.agenda.databinding.ActivityFormularioBinding
import com.example.agenda.dialogos.BorrarDialogo
import com.example.agenda.viewmodel.FormularioViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FormularioActivity : AppCompatActivity(),BorrarDialogo.BorrarListener {
    private val db = FirebaseFirestore.getInstance()
    lateinit var binding: ActivityFormularioBinding
    lateinit var viewModel:FormularioViewModel
    lateinit var dialogoBorrar: BorrarDialogo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogoBorrar = BorrarDialogo(this)
        viewModel = ViewModelProvider(this).get()
        viewModel.operacion = intent.getStringExtra(Constantes.OPERACION_KEY)!!
        binding.modelo = viewModel
        binding.lifecycleOwner = this

        viewModel.operacionExistosa.observe(this, Observer {
            if (it){
                mostrarMensaje("Operacion exitosa")
                irAlInicio()
            }else{
                mostrarMensaje("Error")
            }
        })

        if (viewModel.operacion.equals(Constantes.OPERACION_EDITAR)){
            viewModel.id.value = intent.getLongExtra(Constantes.ID_PERSONAL_KEY,0)
            viewModel.cargarDatos()
            binding.linearEditar.visibility = View.VISIBLE
            binding.btnguardar.visibility = View.GONE

        }else{
            binding.linearEditar.visibility = View.GONE
            binding.btnguardar.visibility = View.VISIBLE
        }

        binding.btnBorar.setOnClickListener {
            mostrarDialogo()
        }
            //botones para firestore
        binding.btnguardar.setOnClickListener {
            db.collection("users")
                .document(binding.etEmail.toString()).set(
                    hashMapOf("nombre" to binding.etNombre.text.toString(),
                    "apellido" to  binding.etApellidos.text.toString(),
                    "edad" to binding.etEdad.text.toString(),
                    "telefono" to binding.etTelefono.text.toString())
                )
        }


    }

    private fun mostrarDialogo() {
        dialogoBorrar.show(supportFragmentManager, "Dialogo Borrar")
    }

    private fun irAlInicio() {
        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun mostrarMensaje(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun borrarPersonal() {
        viewModel.eliminarPersonal()
    }
}