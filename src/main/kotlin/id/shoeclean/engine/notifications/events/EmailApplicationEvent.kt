package id.shoeclean.engine.notifications.events

import org.springframework.context.ApplicationEvent

/**
 * The application event class related to email.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
abstract class EmailApplicationEvent(recipient: String) : ApplicationEvent(recipient) {
    fun getRecipient(): String = source as String
}
