package id.shoeclean.engine.accounts

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Account].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
interface AccountRepository : JpaRepository<Account, Long> {
}
