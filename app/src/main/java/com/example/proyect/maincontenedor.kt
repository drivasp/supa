package com.example.proyect

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyect.Models.Alumnno
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

        val actvMaterias = findViewById<AutoCompleteTextView>(R.id.listaMaterias)
        val lvAlumnos = findViewById<ListView>(R.id.lvmateria)

        actvMaterias.setText("")
        val lstMaterias = ArrayList<String>()
        val lstAlumnos = ArrayList<String>()

        lifecycleScope.launch {

            try {
                val listaMaterias = ArrayList(
                    SupabaseManager.client
                        .from("materias")
                        .select {
                            filter {
                                eq("nivel", 6)
                            }
                            order("nombre", Order.ASCENDING)
                        }
                        .decodeList<Materia>()
                )
                for (materia in listaMaterias) {
                    lstMaterias.add(materia.nombre ?: "")
                }
            } catch (e: RestException) {
                SupabaseErrorHandler.show(this@maincontenedor, e)
                lstMaterias.clear()
            } finally {
                val adapter = ArrayAdapter(
                    this@maincontenedor,
                    android.R.layout.simple_spinner_dropdown_item,
                    lstMaterias
                )
                actvMaterias.setAdapter(adapter)
            }

            try {
                val listaAlumnos = ArrayList(
                    SupabaseManager.client
                        .from("alumnos")
                        .select {
                            order("nombres", Order.ASCENDING)
                        }
                        .decodeList< Alumnno>()
                )
                for (alumno in listaAlumnos) {
                    lstAlumnos.add(alumno.nombres ?: "")
                }
            } catch (e: RestException) {
                SupabaseErrorHandler.show(this@maincontenedor, e)
                lstAlumnos.clear()
            } finally {
                val adapter2 = ArrayAdapter(
                    this@maincontenedor,
                    android.R.layout.simple_list_item_1,
                    lstAlumnos
                )
                lvAlumnos.setAdapter(adapter2)
            }
        }
    }
}