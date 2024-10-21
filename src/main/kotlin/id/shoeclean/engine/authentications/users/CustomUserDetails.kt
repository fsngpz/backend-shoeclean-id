package id.shoeclean.engine.authentications.users

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * The custom class for [UserDetails].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
class CustomUserDetails(
    private val id: Long,
    private val email: String,
    private val password: String,
    private var authorities: List<GrantedAuthority>
) : UserDetails {
    /**
     * a function to get the id.
     *
     * @return the id of user.
     */
    fun getId(): Long {
        return id
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
