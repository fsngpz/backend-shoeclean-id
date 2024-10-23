package id.shoeclean.engine.authentications.passwords

import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.authentications.users.UserService
import id.shoeclean.engine.notifications.events.EmailEventNotificationPublisher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * The test class for [PasswordService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@SpringBootTest(classes = [PasswordService::class])
class PasswordServiceTest(@Autowired private val service: PasswordService) {
    @MockBean
    private lateinit var mockUserService: UserService

    @MockBean
    private lateinit var mockPasswordEncoder: PasswordEncoder

    @MockBean
    private lateinit var mockEmailEventNotificationPublisher: EmailEventNotificationPublisher

    @Test
    fun `dependencies are not null`() {
        assertThat(service).isNotNull
        assertThat(mockUserService).isNotNull
        assertThat(mockPasswordEncoder).isNotNull
        assertThat(mockEmailEventNotificationPublisher).isNotNull
    }

    @Test
    fun `forgetPassword, success`() {
        val mockUser = mock<User>()
        // -- mock --
        whenever(mockUserService.getByEmail(any<String>())).thenReturn(mockUser)

        // -- execute --
        assertAll({ service.forgetPassword("johndoe@gmail.com") })

        // -- verify --
        verify(mockUserService).getByEmail(any<String>())
        verify(mockUserService).createTokenUid(any<User>())
        verify(mockEmailEventNotificationPublisher).publishEmailForgotPassword(any<String>())
    }

    @Test
    fun `resetPassword, token uid not found`() {
        // -- mock --
        whenever(mockUserService.findByTokenUid(any<String>())).thenReturn(null)

        // -- execute --
        assertThrows<NoSuchElementException> { service.resetPassword("tokenUid", "newpass") }

        // -- verify --
        verify(mockUserService).findByTokenUid(any<String>())
        verify(mockPasswordEncoder, never()).encode(any<String>())
        verify(mockUserService, never()).updateUserPassword(any<User>(), any<String>())
    }

    @Test
    fun `resetPassword, success`() {
        val mockUser = mock<User>()
        // -- mock --
        whenever(mockUserService.findByTokenUid(any<String>())).thenReturn(mockUser)
        whenever(mockPasswordEncoder.encode(any<String>())).thenReturn("n3wpa$$")

        // -- execute --
        assertAll({ service.resetPassword("tokenUid", "newpass") })

        // -- verify --
        verify(mockUserService).findByTokenUid(any<String>())
        verify(mockPasswordEncoder).encode(any<String>())
        verify(mockUserService).updateUserPassword(any<User>(), any<String>())
    }
}
