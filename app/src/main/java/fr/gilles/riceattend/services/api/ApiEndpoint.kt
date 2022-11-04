package fr.gilles.riceattend.services.api


import fr.gilles.riceattend.services.repositories.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiEndpoint @Inject constructor(
    val authRepository: AuthRepository = ApiService.retrofit.create(AuthRepository::class.java),
    val workerRepository: WorkerRepository = ApiService.retrofit.create(WorkerRepository::class.java),
    val resourceRepository: ResourceRepository = ApiService.retrofit.create(ResourceRepository::class.java),
    val paddyFieldRepository: PaddyFieldRepository = ApiService.retrofit.create(PaddyFieldRepository::class.java),
    val activityRepository: ActivityRepository = ApiService.retrofit.create(ActivityRepository::class.java),
    val statisticsRepository: StatisticsRepository = ApiService.retrofit.create(StatisticsRepository::class.java),
)