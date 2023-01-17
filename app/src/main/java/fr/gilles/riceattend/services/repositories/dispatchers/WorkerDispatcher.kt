package fr.gilles.riceattend.services.repositories.dispatchers

import fr.gilles.riceattend.models.*
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.repositories.room.RiceAttendDatabase
import fr.gilles.riceattend.services.storage.RepositoryType
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.callFrom
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Named

class WorkerDispatcher @Inject constructor(
    @Named("room_database") private val roomDatabase: RiceAttendDatabase,
    private val apiEndpoint: ApiEndpoint,
)
{
    fun get(params: Params, onFinish: (data:Page<Worker>)->Unit = {}, onError:()->Unit = {}){
         if(SessionManager.session.repositoryType == RepositoryType.REMOTE){
             apiEndpoint.workerRepository.get(params.toMap())
                 .enqueue(object : ApiCallback<Page<Worker>>() {
                     override fun onSuccess(response: Page<Worker>) {
                         onFinish(response)
                     }
                     override fun onError(error: ApiResponseError) {
                         onError()
                     }
                 })
         } else{
             roomDatabase.workerRepository().get().value?.let {
                 onFinish(pageFromList(it))
             } ?: run {
                 onError()
             }
        }

    }

    fun get(code: String): Call<WorkerDetails> {
        return if(SessionManager.session.repositoryType == RepositoryType.REMOTE)
            apiEndpoint.workerRepository.get(code)
        else{
            val worker = roomDatabase.workerRepository().get(code)
            val activitiesWorkers = listOf<ActivityWorkerWithoutWorker>()
            callFrom(WorkerDetails.fromWorker(worker.value!!, activitiesWorkers))
        }
    }

    fun create(worker: WorkerPayload, onFinish: (data:Worker)->Unit = {}, onError:()->Unit = {}){
         if(SessionManager.session.repositoryType == RepositoryType.REMOTE){
             apiEndpoint.workerRepository.create(worker)
                 .enqueue(object : ApiCallback<Worker>() {
                     override fun onSuccess(response: Worker) {
                         onFinish(response)
                     }

                     override fun onError(error: ApiResponseError) {
                         onError()
                     }
                 })
         }else{
             roomDatabase.workerRepository().insert(worker.toWorker())
             onFinish(worker.toWorker())
         }
    }


    fun update(code: String, workerPayload: WorkerPayload): Call<WorkerDetails> {
        return if(SessionManager.session.repositoryType == RepositoryType.REMOTE)
            apiEndpoint.workerRepository.update(code, workerPayload)
        else{
            val worker = roomDatabase.workerRepository().get(code)
            worker.value!!.updateFromWorkerPayload(workerPayload)
            val activitiesWorkers = listOf<ActivityWorkerWithoutWorker>()
            callFrom(WorkerDetails.fromWorker(roomDatabase.workerRepository().updateAndGet(worker.value!!), activitiesWorkers))
        }
    }
}