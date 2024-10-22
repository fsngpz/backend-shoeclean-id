package id.shoeclean.engine.authentications

import id.shoeclean.engine.authentications.jwt.JwtService
import id.shoeclean.engine.authentications.roles.RoleResponse
import id.shoeclean.engine.authentications.users.CustomUserDetails
import id.shoeclean.engine.authentications.users.UserRoleService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * The test class of [AuthenticationService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@SpringBootTest(classes = [AuthenticationService::class])
class AuthenticationServiceTest(@Autowired private val service: AuthenticationService) {
    @MockBean
    private lateinit var mockJwtService: JwtService

    @MockBean
    private lateinit var mockUserRoleService: UserRoleService

    @MockBean
    private lateinit var mockAuthenticationManager: AuthenticationManager

    @Test
    fun `dependencies are not null`() {
        assertThat(service).isNotNull
        assertThat(mockJwtService).isNotNull
        assertThat(mockUserRoleService).isNotNull
        assertThat(mockAuthenticationManager).isNotNull
    }

    @Test
    fun `login, failed incorrect credentials`() {
        val mockAuthentication = mock<Authentication>()
        // -- mock --
        whenever(mockAuthenticationManager.authenticate(any<Authentication>())).thenReturn(mockAuthentication)
        whenever(mockAuthentication.isAuthenticated).thenReturn(false)

        // -- execute --
        assertThrows<UsernameNotFoundException> { service.login("mail@example.com", "password") }

        // -- verify --
        verify(mockJwtService, never()).generateToken(any<CustomUserDetails>())
    }

    @Test
    fun `login, success`() {
        val mockUser = CustomUserDetails(
            1L,
            "example@example.com",
            "password",
            listOf()
        )
        val bearerToken = "Bearer JWT"
        val mockAuthentication = mock<Authentication>()
        // -- mock --
        whenever(mockAuthenticationManager.authenticate(any<Authentication>())).thenReturn(mockAuthentication)
        whenever(mockAuthentication.isAuthenticated).thenReturn(true)
        whenever(mockAuthentication.principal).thenReturn(mockUser)
        whenever(mockJwtService.generateToken(any<CustomUserDetails>())).thenReturn(bearerToken)

        // -- execute --
        val result = service.login("mail@example.com", "password")
        assertThat(result.bearerToken).isEqualTo(bearerToken)

        // -- verify --
        verify(mockJwtService).generateToken(any<CustomUserDetails>())
    }

    @Test
    fun `getRoles, success`() {
        val mockRole = RoleResponse(listOf("USER"))
        // -- mock --
        whenever(mockUserRoleService.getRoles(any<Long>())).thenReturn(mockRole)
        // -- execute --
        val result = service.getRoles(1L)
        assertThat(result).usingRecursiveComparison().isEqualTo(mockRole)

        // -- verify --
        verify(mockUserRoleService).getRoles(any<Long>())

    }
}
