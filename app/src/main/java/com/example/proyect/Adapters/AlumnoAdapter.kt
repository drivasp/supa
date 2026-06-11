package com.example.proyect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyect.Models.Alumno
import com.example.proyect.R

class AlumnoAdapter(context: Context, var alumnos: ArrayList<Alumno>) :
    ArrayAdapter<Alumno>(context, R.layout.item_alumno, alumnos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_alumno, parent, false)

        val alumno = alumnos[position]

        view.findViewById<TextView>(R.id.txtNombre).text = alumno.nombres
        view.findViewById<TextView>(R.id.txtCorreo).text = alumno.correo
        view.findViewById<TextView>(R.id.txtTelefono).text = alumno.telefono

        Glide.with(context)
            .load("https://sga.uteq.edu.ec" + alumno.foto)
            .circleCrop()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(view.findViewById<ImageView>(R.id.imgAlumno))

        return view
    }
}