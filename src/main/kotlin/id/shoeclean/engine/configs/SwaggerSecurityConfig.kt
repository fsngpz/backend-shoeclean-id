package id.shoeclean.engine.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The security configuration class for Swagger.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Configuration
class SwaggerSecurityConfig {

    /**
     * a function as Bean to setup the [OpenAPI].
     *
     * @return the [OpenAPI] instance.
     */
    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI().apply {
            // -- add the security item --
            this.addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            // -- add the components --
            this.components = Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme())
            // -- add the info --
            this.info = Info().title("Urban Sole Care Backend API")
        }
    }

    /**
     * a private function to create the [SecurityScheme] for Swagger.
     *
     * @return the [SecurityScheme].
     */
    private fun createApiKeyScheme(): SecurityScheme {
        return SecurityScheme().apply {
            this.type = SecurityScheme.Type.HTTP
            this.bearerFormat = "JWT"
            this.scheme = "bearer"
        }
    }
}
