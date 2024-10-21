package id.shoeclean.engine.authentications.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * The entry point class for JWT Auth.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Component
class JwtAuthEntryPoint : AuthenticationEntryPoint {

    /**
     * an override function to start the JWT Auth.
     *
     * @param request the [HttpServletRequest].
     * @param response the [HttpServletResponse].
     * @param authException the [AuthenticationException].
     */
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }
}
