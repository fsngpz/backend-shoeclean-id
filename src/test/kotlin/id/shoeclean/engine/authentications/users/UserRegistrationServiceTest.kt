package id.shoeclean.engine.authentications.users

import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.authentications.registration.AttributeRequest
import id.shoeclean.engine.authentications.roles.Role
import id.shoeclean.engine.authentications.roles.RoleService
import id.shoeclean.engine.exceptions.DuplicateEmailException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * The test class for [UserRegistrationService]
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */

@SpringBootTest(classes = [UserRegistrationService::class])
@Import(value = [ObjectMapper::class])
internal class UserRegistrationServiceTest
    (
    @Autowired private val userRegistrationService: UserRegistrationService
) {
    // -- region of mock --
    @MockBean
    private lateinit var mockUserRepository: UserRepository

    @MockBean
    private lateinit var mockRoleService: RoleService

    @MockBean
    private lateinit var mockUserRoleService: UserRoleService

    @MockBean
    private lateinit var mockPasswordEncoder: PasswordEncoder
    // -- end of region mock --

    @Test
    fun `dependencies are not null`() {
        assertThat(userRegistrationService).isNotNull
        assertThat(mockUserRepository).isNotNull
        assertThat(mockRoleService).isNotNull
        assertThat(mockUserRoleService).isNotNull
        assertThat(mockPasswordEncoder).isNotNull
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "invalid-email",
            "another.test@domain123",
            "user@domain_with_underscore.com"]
    )
    fun `attempting to create with invalid email address`(email: String) {
        // -- execute --
        assertThrows<IllegalArgumentException> {
            userRegistrationService.create(
                email,
                "pass",
                attributes = AttributeRequest(),
                roleName = "ROLE_CUSTOMER"
            )
        }
    }

    @Test
    fun `attempting to create with an exist email address`() {
        val email = "test@domain123.com"
        val mockUser = createMockUser();

        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(mockUser)

        // -- execute --
        assertThrows<DuplicateEmailException> {
            userRegistrationService.create(
                email, "pass", attributes = AttributeRequest(),
                roleName = "ROLE_CUSTOMER"
            )
        }
        // -- verify --
        verify(mockUserRepository, never()).save(any<User>())
    }

    @Test
    fun `attempting to create with valid email address and success`() {
        val email = "test@domain123.com"
        val mockRole = createMockRole()
        // -- mock --
        whenever(mockUserRepository.findByEmail(any<String>())).thenReturn(null)
        whenever(mockPasswordEncoder.encode(any<String>())).thenReturn("pass")
        whenever(mockRoleService.getOrCreate(any<String>(), anyOrNull())).thenReturn(mockRole)

        // -- execute --
        userRegistrationService.create(
            email, "pass", attributes = AttributeRequest(),
            roleName = "ROLE_CUSTOMER"
        )

        // -- verify --
        verify(mockUserRepository).save(any<User>())
    }

    fun createMockUser(): User {
        return User(
            email = "test@mail.com",
            password = "pass"
        )
    }

    fun createMockRole(): Role {
        return Role("ROLE_TEST")
    }
}
