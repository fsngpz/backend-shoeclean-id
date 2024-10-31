package id.shoeclean.engine.authentications.registration

import id.shoeclean.engine.authentications.users.UserRegistrationService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * The test class for [RegistrationService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@SpringBootTest(classes = [RegistrationService::class])
@Disabled
class RegistrationServiceTest(@Autowired private val service: RegistrationService) {
    @MockBean
    private lateinit var mockUserRegistrationService: UserRegistrationService

    @Test
    fun `register, with null email`() {
        val request = RegistrationRequest(null, null, null)
        // -- execute --
        assertThrows<IllegalArgumentException> { service.register(request) }

        // -- verify --
        verify(mockUserRegistrationService, never()).create(
            any<String>(),
            any<String>(),
            any<String>(),
            any<AttributeRequest>()
        )
    }

    @Test
    fun `register, with null password`() {
        val request = RegistrationRequest("mail.com", null, null)
        // -- execute --
        assertThrows<IllegalArgumentException> { service.register(request) }

        // -- verify --
        verify(mockUserRegistrationService, never()).create(
            any<String>(),
            any<String>(),
            any<String>(),
            any<AttributeRequest>()
        )
    }

    @Test
    fun `register, with null attributes`() {
        val request = RegistrationRequest("mail.com", "password", null)
        // -- execute --
        assertThrows<IllegalArgumentException> { service.register(request) }

        // -- verify --
        verify(mockUserRegistrationService, never()).create(
            any<String>(),
            any<String>(),
            any<String>(),
            any<AttributeRequest>()
        )
    }

    @Test
    fun `register, success`() {
        val request = RegistrationRequest("mail.com", "password", AttributeRequest())
        // -- execute --
        assertAll({ service.register(request) })

        // -- verify --
        verify(mockUserRegistrationService).create(
            any<String>(),
            any<String>(),
            any<String>(),
            any<AttributeRequest>()
        )
    }
}
