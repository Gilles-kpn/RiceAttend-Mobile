package fr.gilles.riceattend.services.app

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.entities.models.User
import fr.gilles.riceattend.ui.screens.main.lists.Language
import java.lang.ref.WeakReference


data class Session(
    @SerializedName("authorization") var authorization: String = "",
    @SerializedName("user") var user: User? = null,
    @SerializedName("preferences") var preferences: Map<String, Any> =
        mapOf(
            "theme" to "light",
            "language" to Language.FRENCH
        )
)


class SessionManager {
    companion object {
        var session: Session by mutableStateOf(Session())

        var context: WeakReference<Context> = WeakReference(null)

        fun load() {
            context.get()?.let {
                it.getSharedPreferences("session", Context.MODE_PRIVATE)
                    .getString("session", null)?.let { serializerSession ->
                        this.session = Gson().fromJson(serializerSession, Session::class.java)
                    }

            }
        }

        fun store() {
            context.get()?.getSharedPreferences("session", Context.MODE_PRIVATE)?.edit()
                ?.putString("session", Gson().toJson(session))?.apply()
        }

        fun clear() {
            session = Session()
            context.get()?.getSharedPreferences("session", Context.MODE_PRIVATE)?.edit()
                ?.remove("session")?.apply()
        }
    }
}