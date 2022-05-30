package fr.gilles.riceattend.ui.formfields

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.security.cert.CertPathValidator
import java.util.regex.Pattern

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorMessage: (String) ->String
) {
    var text by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun validate() {
        error = if (validator(text)) null else errorMessage(text)
    }
}

class EmailFieldState :TextFieldState(
    validator = ::isValidEmail,
    errorMessage = ::emailErrorMessage
) {
}

class PasswordFieldState : TextFieldState(
    validator = ::isValidPassword,
    errorMessage = { "Password must be at least 8 characters long" }
) {
}

private fun isValidPassword(password: String): Boolean {
    return Pattern.matches(PASSWORDREGEX, password)
}
private const val EMAILREGEX = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
private const val PASSWORDREGEX  = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&.]{8,}$"
private fun isValidEmail(email: String): Boolean {
    return Pattern.matches(EMAILREGEX, email)
}
private fun emailErrorMessage(email:String):String{
    return "Email $email is not valid"
}


