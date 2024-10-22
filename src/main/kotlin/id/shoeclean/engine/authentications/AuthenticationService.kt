package id.shoeclean.engine.authentications

import id.shoeclean.engine.authentications.jwt.JwtService
import id.shoeclean.engine.authentications.roles.RoleResponse
import id.shoeclean.engine.authentications.users.CustomUserDetails
import id.shoeclean.engine.authentications.users.UserRoleService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * The service class for authentication feature.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */

@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val userRoleService: UserRoleService,
    private val authenticationManager: AuthenticationManager
) {

    /**
     * a function to handle request login.
     *
     * @param email the email of user.
     * @param password the password of user.
     * @return the JWT token in [AuthenticationResponse].
     */
    fun login(email: String, password: String): AuthenticationResponse {
        // -- setup the instance for email and password --
        val request = UsernamePasswordAuthenticationToken(email, password)
        // -- check is authenticated --
        val authentication = authenticationManager.authenticate(request)
        // -- check is user authenticated or not. if not, an exception will be thrown --
        require(authentication.isAuthenticated) {
            throw UsernameNotFoundException("invalid username and password!")
        }
        // -- get the principal from Authentication and cast as CustomUserDetails --
        val user = authentication.principal as CustomUserDetails
        // -- get the authority from authorities --
        val authorities = user.authorities.map(GrantedAuthority::getAuthority)
        // -- generate the token --
        val bearerToken = jwtService.generateToken(user)
        // -- return the token --
        return AuthenticationResponse(bearerToken, authorities)
    }

    /**
     * a function to handle get the [Role] of [User] by user unique identifier.
     *
     * @param userId the user unique identifier.
     * @return the [RoleResponse] instance.
     */
    fun getRoles(userId: Long): RoleResponse {
        return userRoleService.getRoles(userId);
    }
}
