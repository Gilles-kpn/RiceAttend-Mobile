package fr.gilles.riceattend.services.storage

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.models.User
import fr.gilles.riceattend.ui.screens.pages.lists.Language
import java.lang.ref.WeakReference


enum class RepositoryType {
    LOCAL, REMOTE, NONE
}
enum class RiceAttendTheme(val value: String, val label: String) {
    LIGHT("light", "Clair"),
    DARK("dark", "Sombre"),
    SYSTEM("system", "Systeme")


}
fun themeFromString(value: String): RiceAttendTheme {
    return when (value) {
        "light" -> RiceAttendTheme.LIGHT
        "dark" -> RiceAttendTheme.DARK
        "system" -> RiceAttendTheme.SYSTEM
        else -> RiceAttendTheme.SYSTEM
    }
}
data class Session(
    @SerializedName("authorization") var authorization: String = "",
    @SerializedName("user") var user: User? = null,
    @SerializedName("repositoryType") var repositoryType: RepositoryType =RepositoryType.NONE,
    @SerializedName("preferences") var preferences: Map<String, String> = mapOf(
        "language" to Language.FRENCH.value,
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


        fun setRepositoryType(repositoryType: RepositoryType) {
            session = session.copy(repositoryType = repositoryType)
            store()
        }


        fun initSession(contextRef: WeakReference<Context>) {
            context = contextRef
            load()
        }


        fun toggleRepositoryType() {
            session.repositoryType = if (session.repositoryType == RepositoryType.REMOTE) RepositoryType.LOCAL else RepositoryType.REMOTE
            store()
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