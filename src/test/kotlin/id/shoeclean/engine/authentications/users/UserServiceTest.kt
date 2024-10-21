package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.exceptions.UserNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * The test class of [UserService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@SpringBootTest(classes = [UserService::class])
class UserServiceTest(
    @Autowired private val userService: UserService
) {

    @MockBean
    private lateinit var mockUserRepository: UserRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(userService).isNotNull
        assertThat(mockUserRepository).isNotNull
    }

    @Test
    fun `isEmailAlreadyExist, return true`() {
        val mockUser = mock<User>()
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(mockUser)

        // -- execute --
        val result = userService.isEmailAlreadyExist("email@email.com")
        assertThat(result).isEqualTo(true)

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }

    @Test
    fun `isEmailAlreadyExist, return false`() {
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(null)

        // -- execute --
        val result = userService.isEmailAlreadyExist("email@email.com")
        assertThat(result).isEqualTo(false)

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }

    @Test
    fun `findByEmail, return null`() {
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(null)

        // -- execute --
        val result = userService.findByEmail("email@email.com")
        assertThat(result).isNull()

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }

    @Test
    fun `findByEmail, return User instance`() {
        val mockUser = mock<User>()
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(mockUser)

        // -- execute --
        val result = userService.findByEmail("email@email.com")
        assertThat(result).isNotNull
        assertThat(result).usingRecursiveComparison().isEqualTo(mockUser)

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }

    @Test
    fun `getByEmail, return User instance`() {
        val mockUser = mock<User>()
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(mockUser)

        // -- execute --
        val result = userService.getByEmail("email@email.com")
        assertThat(result).isNotNull
        assertThat(result).usingRecursiveComparison().isEqualTo(mockUser)

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }

    @Test
    fun `getByEmail, email not found`() {
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(null)

        // -- execute --
        assertThrows<UserNotFoundException> { userService.getByEmail("email@email.com") }

        // -- verify --
        verify(mockUserRepository).findByEmail(any<String>())
    }
}
