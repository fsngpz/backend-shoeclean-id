package id.shoeclean.engine.notifications.events

import id.shoeclean.engine.notifications.EmailSenderService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * The listener class to handle the email notification.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Component
class EmailNotificationEventListener(private val emailSenderService: EmailSenderService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * a function to handle event of [EmailRegistrationApplicationEvent].
     *
     * @param event the [EmailRegistrationApplicationEvent] instance
     */
    @EventListener
    @Async
    fun handleEmailRegistrationEvent(event: EmailRegistrationApplicationEvent) {
        val recipient = event.getRecipient()
        logger.info("Received EmailRegistrationApplicationEvent for: $recipient")
        emailSenderService.sendRegistrationMail(recipient)
        logger.info("Processed EmailRegistrationApplicationEvent for: $recipient")
    }

    /**
     * a function to handle event of [EmailForgotPasswordApplicationEvent].
     *
     * @param event the [EmailForgotPasswordApplicationEvent] instance
     */
    @EventListener
    @Async
    fun handleEmailForgotPasswordEvent(event: EmailForgotPasswordApplicationEvent) {
        val recipient = event.getRecipient()
        logger.info("Received EmailForgotPasswordApplicationEvent for: $recipient")
        emailSenderService.sendForgotPasswordMail(recipient)
        logger.info("Processed EmailForgotPasswordApplicationEvent for: $recipient")
    }
}
