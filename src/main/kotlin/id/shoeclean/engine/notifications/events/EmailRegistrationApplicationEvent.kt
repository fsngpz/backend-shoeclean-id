package id.shoeclean.engine.notifications.events

/**
 * The application event class for notification email registration.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
class EmailRegistrationApplicationEvent(recipient: String) : EmailApplicationEvent(recipient)
