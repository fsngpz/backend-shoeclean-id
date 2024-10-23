package id.shoeclean.engine.accounts

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import id.shoeclean.engine.authentications.registration.AttributeRequest
import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.exceptions.AccountNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related [Account].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Service
class AccountService(private val accountRepository: AccountRepository, private val objectMapper: ObjectMapper) {

    /**
     * a function to handle request retrieving instance of [Account].
     *
     * @param id the unique identifier of account.
     * @return the [Account] instance.
     */
    fun get(id: Long): Account {
        // -- find the Account by id or else throw an exception --
        return accountRepository.findByIdOrNull(id) ?: throw AccountNotFoundException(
            "no account was found with id '$id'"
        )
    }

    /**
     * a function to handle request create new [Account] instance.
     *
     * @param user the [User] instance.
     * @return the created [Account] instance.
     */
    fun create(user: User): Account {
        val stringAttributes = user.attributes
        // -- convert the stringAttributes to object --
        val attributes = if (stringAttributes != null) {
            objectMapper.readValue<AttributeRequest>(stringAttributes)
        } else {
            AttributeRequest()
        }
        // -- setup instance Account --
        val account = Account(user).apply {
            this.name = attributes.name
        }
        // -- save the instance to database --
        return accountRepository.save(account)
    }

}
