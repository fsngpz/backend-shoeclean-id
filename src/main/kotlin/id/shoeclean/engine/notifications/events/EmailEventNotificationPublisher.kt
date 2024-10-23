package id.shoeclean.engine.notifications.events

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

/**
 * The publisher event for email notification.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Component
class EmailEventNotificationPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * a function to publish the email registration event.
     *
     * @param recipient the recipient email address.
     */
    fun publishEmailRegistration(recipient: String) {
        logger.info("Starting to publish email registration event : {}", recipient)
        // -- setup the application event --
        val event = EmailRegistrationApplicationEvent(recipient)
        // -- publish the event --
        applicationEventPublisher.publishEvent(event)
        logger.info("Finished to publish email registration event : {}", recipient)
    }

    /**
     * a function to publish the email forgot password event.
     *
     * @param recipient the recipient email address.
     */
    fun publishEmailForgotPassword(recipient: String) {
        logger.info("Starting to publish email forgot password event : {}", recipient)
        // -- setup the application event --
        val event = EmailForgotPasswordApplicationEvent(recipient)
        // -- publish the event --
        applicationEventPublisher.publishEvent(event)
        logger.info("Finished to publish email forgot password event : {}", recipient)
    }
}
