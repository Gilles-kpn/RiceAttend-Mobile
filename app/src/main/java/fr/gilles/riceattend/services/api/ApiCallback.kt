package fr.gilles.riceattend.services.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.storage.RepositoryType
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body()!!)
        } else {
            val apiResponseError: ApiResponseError? = Gson().fromJson(
                (response.errorBody())?.string(),
                ApiResponseError::class.java
            )
            apiResponseError?.let {
                onError(it)
            } ?: run {
                onError(
                    ApiResponseError(
                        "",
                        "",
                        "",
                        0,
                        ""
                    )
                )
            }

        }
    }

    abstract fun onSuccess(response: T)
    abstract fun onError(error: ApiResponseError)


    override fun onFailure(call: Call<T>, t: Throwable) {
        if(SessionManager.session.repositoryType == RepositoryType.REMOTE)
            DialogService.show(
                Dialog(
                    title = "Erreur",
                    message = "Une erreur est survenue. Veuillez verifier que vous disposez d'une connexion internet et reessayer. Si le probleme persiste, contactez nous.",
                    displayDismissButton = false
                )
            )
    }
}

data class ApiResponseError(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String,
    @SerializedName("status") val status: Int,
    @SerializedName("timestamp") val timestamp: String
)
