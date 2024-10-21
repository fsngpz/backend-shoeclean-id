package id.shoeclean.engine.authentications.registration

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

/**
 * The test class of [RegistrationController].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@WebMvcTest(controllers = [RegistrationController::class])
internal class RegistrationControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var mockRegistrationService: RegistrationService

    @Test
    fun `dependencies are not null`() {
        assertThat(mockMvc).isNotNull
        assertThat(mockRegistrationService).isNotNull
    }

    @Test
    fun `register success`() {
        val request = RegistrationRequest("email@example.com", "password", AttributeRequest())
        // -- execute --
        mockMvc.post("/v1/auth/registration") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andDo {
            print()
        }.andExpect {
            status { isCreated() }
        }

        // -- verify --
        verify(mockRegistrationService).register(any<RegistrationRequest>())
    }
}
