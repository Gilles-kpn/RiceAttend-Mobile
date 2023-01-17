package fr.gilles.riceattend.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
@Entity(tableName = "workers")
data class Worker(
    @ColumnInfo(name = "name") @SerializedName("name") var name: String,
    @ColumnInfo(name = "first_name") @SerializedName("firstName") var firstName: String,
    @ColumnInfo(name = "last_name") @SerializedName("lastName") var lastName: String,
    @Embedded @SerializedName("address") var address: Address,
    @ColumnInfo(name = "email") @SerializedName("email") var email: String,
    @ColumnInfo(name = "hourly_pay") @SerializedName("hourlyPay") var hourlyPay: Int,
    @ColumnInfo(name = "phone") @SerializedName("phone") var phone: String
) : Audit(){
    fun updateFromWorkerPayload(payload: WorkerPayload){
        name = payload.name
        firstName = payload.firstName
        lastName = payload.lastName
        address = payload.address
        email = payload.email
        hourlyPay = payload.hourlyPay
        phone = payload.phone
    }
}

data class WorkerDetails(
    val name: String,
    @ColumnInfo(name = "first_name") @SerializedName("firstName") var firstName: String,
    @ColumnInfo(name = "last_name") @SerializedName("lastName") var lastName: String,
    @Embedded @SerializedName("address") var address: Address,
    @ColumnInfo(name = "email") @SerializedName("email") var email: String,
    @ColumnInfo(name = "hourly_pay") @SerializedName("hourlyPay") var hourlyPay: Int,
    @ColumnInfo(name = "phone") @SerializedName("phone") var phone: String,
    @Relation(
        parentColumn = "code",
        entityColumn = "code",
        entity = ActivityWorkerWithoutWorker::class
    ) @SerializedName("activityWorkers") var activityWorkers: List<ActivityWorkerWithoutWorker>
): Audit(){


    //static method to convert from WorkerDetails to Worker
    companion object{
        fun fromWorker(worker: Worker,activities: List<ActivityWorkerWithoutWorker>): WorkerDetails{
            return WorkerDetails(
                worker.name,
                worker.firstName,
                worker.lastName,
                worker.address,
                worker.email,
                worker.hourlyPay,
                worker.phone,
                activities
            )
        }
    }
}

data class WorkerPayload(
    @SerializedName("name") var name: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String,
    @SerializedName("address") var address: Address,
    @SerializedName("email") var email: String,
    @SerializedName("hourlyPay") var hourlyPay: Int,
    @SerializedName("phone") var phone: String
){
    fun toWorker(): Worker{
        return Worker(
            name,
            firstName,
            lastName,
            address,
            email,
            hourlyPay,
            phone
        )
    }


}

