package id.shoeclean.engine.notifications.events

/**
 * The application event class for notification forgot password.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
class EmailForgotPasswordApplicationEvent(recipient: String) : EmailApplicationEvent(recipient)
