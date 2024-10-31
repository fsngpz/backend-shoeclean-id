package id.shoeclean.engine.transaction

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
interface TransactionRepository : JpaRepository<Transaction, Long>
