package id.shoeclean.engine.authentications.users

import jakarta.transaction.Transactional
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * The service class for [CustomUserDetails]. This class also implements the [UserDetailsService]
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Component
class CustomUserDetailService(private val userService: UserService) : UserDetailsService {

    /**
     * an override function to load the [UserDetails] by email address.
     *
     * @param email the email address.
     * @return the [UserDetails] instance.
     */
    @Transactional
    override fun loadUserByUsername(email: String): UserDetails {
        // -- find the user by email --
        val user = Optional.ofNullable(userService.findByEmail(email))
        // -- map the user to the UserDetail --
        return user.map {
            CustomUserDetails(
                requireNotNull(it.id),
                it.email,
                it.password,
                it.roles.toGrantedAuthorities()
            )
        }.orElseThrow { UsernameNotFoundException("user with email '$email' not found") }
    }


    /**
     * a private function to convert the List of [Role] to List of [GrantedAuthority].
     *
     * @return the List of [GrantedAuthority].
     */
    private fun Set<UserRole>.toGrantedAuthorities(): List<GrantedAuthority> {
        return this.map { SimpleGrantedAuthority(it.role.name) }
    }
}
