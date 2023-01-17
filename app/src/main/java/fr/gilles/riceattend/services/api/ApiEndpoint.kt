package fr.gilles.riceattend.services.api


import fr.gilles.riceattend.services.repositories.retrofit.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiEndpoint @Inject constructor(
    val authRepository: AuthRepository,
    val workerRepository: WorkerRepository,
    val resourceRepository: ResourceRepository,
    val paddyFieldRepository: PaddyFieldRepository,
    val activityRepository: ActivityRepository,
    val statisticsRepository: StatisticsRepository,
)