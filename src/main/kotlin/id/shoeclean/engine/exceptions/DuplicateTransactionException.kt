package id.shoeclean.engine.exceptions

import id.shoeclean.engine.transaction.Transaction

/**
 * The custom exception class to indicate the [Transaction] is duplicated.
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-19
 */
class DuplicateTransactionException(message: String) : RuntimeException(message)
