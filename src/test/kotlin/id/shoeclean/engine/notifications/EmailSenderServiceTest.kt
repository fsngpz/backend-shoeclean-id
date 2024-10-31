package id.shoeclean.engine.notifications

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * The test class of [EmailSenderService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@SpringBootTest
@Disabled
class EmailSenderServiceTest(@Autowired private val service: EmailSenderService) {

    @Test
    fun testSend() {
        service.sendRegistrationMail("fsangap18@gmail.com")
    }
}
