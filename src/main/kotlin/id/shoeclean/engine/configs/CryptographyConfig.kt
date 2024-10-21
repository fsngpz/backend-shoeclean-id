package id.shoeclean.engine.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * The configuration class for any bean cryptography.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Configuration
class CryptographyConfig {

    /**
     * a function act as a bean of [BCryptPasswordEncoder].
     *
     * @return the [BCryptPasswordEncoder].
     */
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

