package id.shoeclean.engine.configs

import id.shoeclean.engine.authentications.jwt.JwtAuthEntryPoint
import id.shoeclean.engine.authentications.jwt.JwtAuthFilter
import id.shoeclean.engine.authentications.users.CustomUserDetailService
import id.shoeclean.engine.authentications.users.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

/**
 * The configuration for Web Security.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userService: UserService,
    private val jwtAuthFilter: JwtAuthFilter,
    private val authEntryPoint: JwtAuthEntryPoint
) {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return CustomUserDetailService(userService)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { }
            .csrf { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint(authEntryPoint)
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                // -- root --
                it.requestMatchers("/").permitAll()
                // -- swagger --
                it.requestMatchers("/swagger-ui/**", "/swagger-ui**", "/v3/api-docs/**", "/v3/api-docs**", "/v3/**")
                    .permitAll()

                // -- auth --
                it.requestMatchers(
                    "/v1/auth/**"
                ).permitAll()
                // -- backoffice --
                it.requestMatchers("/v1/backoffice/**").hasAnyRole(ROLE_ADMIN)
                it.requestMatchers("/v1/**").authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns(
                        "*.urbansolecare.com",
                        "http://localhost:3000",
                        "http://localhost:4173"
                    )
                    .allowedMethods(CorsConfiguration.ALL)
                    .allowedHeaders(CorsConfiguration.ALL)
            }
        }
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean(name = ["mvcHandlerMappingIntrospector"])
    fun mvcHandlerMappingIntrospector(): HandlerMappingIntrospector {
        return HandlerMappingIntrospector()
    }

    companion object {
        const val ROLE_ADMIN = "ADMIN"
    }
}
