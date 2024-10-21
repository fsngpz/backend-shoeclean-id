package id.shoeclean.engine.authentications.roles

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * The test class of [RoleService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@SpringBootTest(classes = [RoleService::class])
internal class RoleServiceTest(@Autowired private val service: RoleService) {
    @MockBean
    private lateinit var mockRoleRepository: RoleRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(service).isNotNull
        assertThat(mockRoleRepository).isNotNull
    }

    @ParameterizedTest
    @ValueSource(strings = ["user test", "hello"])
    fun `getOrCreateRole, creates a new role with description`(description: String?) {
        val roleName = "ROLE_USER"
        val mockRole = mock<Role> { }

        // -- mock --
        whenever(mockRoleRepository.findByNameIgnoreCase(any<String>())).thenReturn(null)
        whenever(mockRoleRepository.save(any<Role>())).thenReturn(mockRole)

        // -- execute --
        assertAll({ service.getOrCreate(roleName, description) })

        // -- verify --
        verify(mockRoleRepository).findByNameIgnoreCase(any<String>())

        // -- captor --
        val roleCaptor = argumentCaptor<Role>()
        verify(mockRoleRepository).save(roleCaptor.capture())
        val capturedRole = roleCaptor.firstValue
        assertThat(capturedRole.name).isEqualTo(roleName)
        assertThat(capturedRole.description).isEqualTo(description)
    }

    @Test
    fun `getOrCreateRole, return the existing role`() {
        val roleName = "ROLE_USER"
        val mockRole = Role(roleName)

        // -- mock --
        whenever(mockRoleRepository.findByNameIgnoreCase(any<String>())).thenReturn(mockRole)
        whenever(mockRoleRepository.save(any<Role>())).thenReturn(mockRole)

        // -- execute --
        val result = service.getOrCreate(roleName)
        assertThat(result).usingRecursiveComparison().isEqualTo(mockRole)

        // -- verify --
        verify(mockRoleRepository).findByNameIgnoreCase(any<String>())
        verify(mockRoleRepository).save(any<Role>())

    }
}
