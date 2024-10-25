package id.shoeclean.engine.addresses

import id.shoeclean.engine.accounts.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * The interface represent the repository of [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
interface AddressRepository : JpaRepository<Address, Long> {

    /**
     * a method to find the [Address] by the filter.
     *
     * @param accountId the account unique identifier.
     * @param filter the parameter to find the [Address] by label, line, city, district, subdistrict or state.
     * @param pageable the [Pageable] instance.
     * @return the [Page] of [Address].
     */
    @Query(
        value = """
        FROM Address a
        WHERE a.account.id = :accountId
        AND (:filter IS NULL OR a.label = :filter)
        OR (:filter IS NULL OR a.line = :filter)
        OR (:filter IS NULL OR a.city = :filter)
        OR (:filter IS NULL OR a.district = :filter)
        OR (:filter IS NULL OR a.subdistrict = :filter)
        OR (:filter IS NULL OR a.state = :filter)
    """
    )
    fun findAllByFilter(accountId: Long, filter: String?, pageable: Pageable): Page<Address>

    /**
     * a method to find the [Address] by account instance and address id.
     *
     * @param id the address unique identifier.
     * @param account the [Account] instance.
     * @return
     */
    fun findByIdAndAccount(id: Long, account: Account): Address?
}
