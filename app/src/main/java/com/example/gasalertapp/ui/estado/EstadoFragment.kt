package com.example.gasalertapp.ui.estado

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gasalertapp.R
import com.example.gasalertapp.databinding.FragmentEstadoBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class EstadoFragment : Fragment() {

    private var _binding: FragmentEstadoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val estados = arrayOf(
        R.drawable.icon_seguro_gas,    // Verde (Seguro)
        R.drawable.icon_alarma_gas, // Amarillo (Alarma)
        R.drawable.icon_peligro_gas   // Rojo (Peligro)
    )

    private val textos = arrayOf(
        "No se detectan fugas",
        "Fuga detectada - ¡Atención!",
        "¡Peligro extremo - Actúe inmediatamente!"
    )

    private var currentStateIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(EstadoViewModel::class.java)

        _binding = FragmentEstadoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa los elementos de la UI
        val statusIcon: ImageView = binding.iconEstado
        val statusText: TextView = binding.txtEstado
        val changeStateButton: Button = binding.btnPrueba

        // Configura el botón para cambiar el ícono
        changeStateButton.setOnClickListener {
            // Cambia al siguiente estado
            currentStateIndex = (currentStateIndex + 1) % estados.size
            statusIcon.setImageResource(estados[currentStateIndex])
            statusText.text = textos[currentStateIndex]
        }
        val database =
            FirebaseDatabase.getInstance("https://gasalert-c58b6-default-rtdb.firebaseio.com")
        val myRef = database.getReference("/Usuarios/usuario1")


        //myRef.setValue("Hello, World!");
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Cambia al siguiente estado
                currentStateIndex = (currentStateIndex + 1) % estados.size
                statusIcon.setImageResource(estados[currentStateIndex])
                statusText.text = textos[currentStateIndex]
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}