package com.example.proyect

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyect.Adapters.AlumnoAdapter
import com.example.proyect.Models.Alumno
import com.example.proyect.Models.Materia
import com.example.proyect.service.SupabaseErrorHandler
import com.example.proyect.service.SupabaseManager
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class maincontenedor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maincontenedor)

        val actvNiveles = findViewById<AutoCompleteTextView>(R.id.listaCategorias)
        val actvMaterias = findViewById<AutoCompleteTextView>(R.id.listaMaterias)
        val lvAlumnos = findViewById<ListView>(R.id.lvmateria)

        actvNiveles.setOnItemClickListener { _, _, position, _ ->
            actvMaterias.setText("")
            val lstMaterias = ArrayList<String>()
            lifecycleScope.launch {
                try {
                    val listaMaterias = ArrayList(
                        SupabaseManager.client
                            .from("materias")
                            .select {
                                filter { eq("nivel", position + 1) }
                                order("nombre", Order.ASCENDING)
                            }
                            .decodeList<Materia>()
                    )
                    for (materia in listaMaterias) lstMaterias.add(materia.nombre ?: "")
                } catch (e: RestException) {
                    SupabaseErrorHandler.show(this@maincontenedor, e)
                    lstMaterias.clear()
                } finally {
                    actvMaterias.setAdapter(
                        ArrayAdapter(
                            this@maincontenedor,
                            android.R.layout.simple_spinner_dropdown_item,
                            lstMaterias
                        )
                    )
                }
            }
        }

        actvMaterias.setOnItemClickListener { _, _, _, _ ->
            lifecycleScope.launch {
                var lstAlumnos = ArrayList<Alumno>()
                try {
                    lstAlumnos = ArrayList(
                        SupabaseManager.client
                            .from("alumnos")
                            .select { order("nombres", Order.ASCENDING) }
                            .decodeList<Alumno>()
                    )
                } catch (e: RestException) {
                    SupabaseErrorHandler.show(this@maincontenedor, e)
                } finally {
                    lvAlumnos.adapter = AlumnoAdapter(this@maincontenedor, lstAlumnos)
                }
            }
        }
    }
}