package fr.gilles.riceattend.ui.formfields

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.regex.Pattern

open class TextFieldState<T>(
    private val validator: (T) -> Boolean = { true },
    private val errorMessage: (T) -> String,
    private val defaultValue: T
) {
    var value by mutableStateOf(defaultValue)
    var error by mutableStateOf<String?>(null)

    fun isValid(): Boolean {
        return (error == null) && validator(value)
    }

    fun validate() {
        error = if (validator(value)) {
            null
        } else {
            errorMessage(value)
        }
    }
}

class EmailFieldState : TextFieldState<String>(
    validator = ::isValidEmail,
    errorMessage = ::emailErrorMessage,
    defaultValue = ""
)

class PasswordFieldState : TextFieldState<String>(
    validator = ::isValidPassword,
    errorMessage = { "Password must be at least 8 characters long" },
    defaultValue = ""
)


private fun isValidPassword(password: String): Boolean {
    return Pattern.matches(PASSWORDREGEX, password)
}

private const val EMAILREGEX =
    "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
private const val PASSWORDREGEX =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&.]{8,}$"

private fun isValidEmail(email: String): Boolean {
    return Pattern.matches(EMAILREGEX, email)
}

private fun emailErrorMessage(email: String): String {
    return "Email $email non valide"
}


