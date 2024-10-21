package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.authentications.roles.Role
import id.shoeclean.engine.exceptions.UserNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

/**
 * The test class of [UserRoleService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@SpringBootTest(classes = [UserRoleService::class])
internal class UserRoleServiceTest(@Autowired val service: UserRoleService) {
    @MockBean
    private lateinit var mockUserRoleRepository: UserRoleRepository

    @MockBean
    private lateinit var mockUserRepository: UserRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(service).isNotNull
        assertThat(mockUserRoleRepository).isNotNull
        assertThat(mockUserRepository).isNotNull

    }

    @Test
    fun `findAll, returning empty`() {
        val userRoles = PageImpl<UserRole>(listOf())
        // -- mock --
        whenever(mockUserRoleRepository.findAllCustom(anyOrNull(), anyOrNull(), any<Pageable>())).thenReturn(
            userRoles
        )

        // -- execute --
        val result = service.findAll(null, null, Pageable.unpaged())
        assertThat(result).isEmpty()

        // -- verify --
        verify(mockUserRoleRepository).findAllCustom(anyOrNull(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `findAll, returning data`() {
        val mockUserRoleOne = mock<UserRole>()
        val mockUserRoleTwo = mock<UserRole>()
        val mockUserRoleThree = mock<UserRole>()
        val userRoles = PageImpl<UserRole>(listOf(mockUserRoleOne, mockUserRoleTwo, mockUserRoleThree))
        // -- mock --
        whenever(mockUserRoleRepository.findAllCustom(anyOrNull(), anyOrNull(), any<Pageable>())).thenReturn(
            userRoles
        )
        // -- execute --
        val result = service.findAll(null, null, Pageable.unpaged())
        assertThat(result).isNotEmpty.hasSize(3)

        // -- verify --
        verify(mockUserRoleRepository).findAllCustom(anyOrNull(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `getRoles, user not found`() {
        // -- mock --
        whenever(mockUserRepository.findById(any<Long>())).thenReturn(Optional.empty())

        // -- execute --
        assertThrows<UserNotFoundException> { service.getRoles(1) }

        // -- verify --
        verify(mockUserRepository).findById(any<Long>())
        verify(mockUserRoleRepository, never()).findAllByUser(any<User>())

    }

    @Test
    fun `getRoles, success with role`() {
        val mockUser = mock<User>()
        val mockRole = Role("USER")
        val mockUserRole = UserRole(mockUser, mockRole)
        // -- mock --
        whenever(mockUserRepository.findById(any<Long>())).thenReturn(Optional.of(mockUser))
        whenever(mockUserRoleRepository.findAllByUser(any<User>())).thenReturn(listOf(mockUserRole))

        // -- execute --
        val result = service.getRoles(1)
        assertThat(result.roles).hasSize(1)

        // -- verify --
        verify(mockUserRepository).findById(any<Long>())
        verify(mockUserRoleRepository).findAllByUser(any<User>())

    }

    @Test
    fun `assign, attempting to duplicate assignment`() {
        val mockUser = mock<User>()
        val mockRole = mock<Role>()
        val mockUserRole = mock<UserRole>()
        // -- mock --
        whenever(mockUserRoleRepository.findByUserAndRole(any<User>(), any<Role>())).thenReturn(mockUserRole)

        // -- execute --
        assertAll({ service.assign(mockUser, mockRole) })

        // -- verify --
        verify(mockUserRoleRepository, never()).save(any<UserRole>())
    }

    @Test
    fun `assign, success without duplicate assignment`() {
        val mockUser = mock<User>()
        val mockRole = mock<Role>()
        // -- mock --
        whenever(mockUserRoleRepository.findByUserAndRole(any<User>(), any<Role>())).thenReturn(null)

        // -- execute --
        assertAll({ service.assign(mockUser, mockRole) })

        // -- verify --
        verify(mockUserRoleRepository).save(any<UserRole>())
    }
}
