package id.shoeclean.engine.authentications

/**
 * a function to check the given email address is matching with the regex.
 *
 * @author Ferdinand Sangap
 * @since 2024-10-21
 */
fun String.isEmailValid(): Boolean {
    val emailRegex = Regex(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    )
    // -- return the matching results of email --
    return emailRegex.matches(this)
}
