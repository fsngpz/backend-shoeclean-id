package id.shoeclean.engine.notifications

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Service
class EmailSenderService(private val mailSender: JavaMailSender) {

    fun sendRegistrationMail(to: String) {
        val subject = "Welcome to the Urban Sole Care, pal!"
        val body =
            """Hi there, we are happy to onboard you as our user!
                    Click here to complete you registration and you are officially become the urban folks!"""
        sendEmail(to, subject, body)
    }

    fun sendForgotPasswordMail(to: String) {
        val subject = "Bad News, You are forgetting you password"
        val body =
            """Hi there, we heard you are requesting to reset the password
                    Click here to change your password mate. Cheers, have a great day!"""
        sendEmail(to, subject, body)
    }

    private fun sendEmail(to: String, subject: String, body: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = body
        mailSender.send(message)
    }
}
