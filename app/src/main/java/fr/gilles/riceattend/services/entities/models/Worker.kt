package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class Worker(
    @SerializedName("name") var name: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String,
    @SerializedName("address") var address: Address,
    @SerializedName("email") var email: String,
    @SerializedName("hourlyPay") var hourlyPay: Int,
    @SerializedName("phone") var phone: String
) : Audit()

data class WorkerDetails(
    @SerializedName("name") var name: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String,
    @SerializedName("address") var address: Address,
    @SerializedName("email") var email: String,
    @SerializedName("hourlyPay") var hourlyPay: Int,
    @SerializedName("phone") var phone: String,
    @SerializedName("activityWorkers") var activityWorkers: List<ActivityWorkerWithoutWorker>
):Audit()

data class WorkerPayload(
    @SerializedName("name") var name: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String,
    @SerializedName("address") var address: Address,
    @SerializedName("email") var email: String,
    @SerializedName("hourlyPay") var hourlyPay: Int,
    @SerializedName("phone") var phone: String
)

