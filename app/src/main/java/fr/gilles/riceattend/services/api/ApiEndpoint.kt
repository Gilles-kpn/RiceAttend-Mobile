package fr.gilles.riceattend.services.api


import android.os.Build
import androidx.annotation.RequiresApi
import fr.gilles.riceattend.services.repositories.*

@RequiresApi(Build.VERSION_CODES.O)
class ApiEndpoint {
    companion object {
        val authRepository = ApiService.buildRepository(AuthRepository::class.java)
        val workerRepository = ApiService.buildRepository(WorkerRepository::class.java)
        val resourceRepository = ApiService.buildRepository(ResourceRepository::class.java)
        val paddyFieldRepository = ApiService.buildRepository(PaddyFieldRepository::class.java)
        val activityRepository = ApiService.buildRepository(ActivityRepository::class.java)
    }
}