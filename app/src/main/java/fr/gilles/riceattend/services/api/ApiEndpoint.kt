package fr.gilles.riceattend.services.api

import fr.gilles.riceattend.services.repositories.AuthRepository

class ApiEndpoint {
    companion object{
        val authRepository = ApiService().buildRepository(AuthRepository::class.java)
    }
}