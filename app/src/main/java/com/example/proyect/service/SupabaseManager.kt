package com.example.proyect.service

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {
    val client = createSupabaseClient(

    ) {
        install(Postgrest)
    }
}