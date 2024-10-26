package id.shoeclean.engine.sneakers

import id.shoeclean.engine.accounts.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * The interface represent the repository of [Sneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
interface SneakerRepository : JpaRepository<Sneaker, Long> {

    /**
     * a method to find the [Sneaker] by the ID and the [Account].
     *
     * @param id the account unique identifier.
     * @param account the [Account] instance.
     * @return the [Sneaker] or null.
     */
    fun findByIdAndAccount(id: Long, account: Account): Sneaker?

    /**
     * a method to find the [Sneaker] by the list of sneaker id and the [Account].
     *
     * @param ids the list of sneaker unique identifier.
     * @param account the [Account] instance.
     * @return the [List] of [Sneaker].
     */
    fun findByIdInAndAccount(ids: List<Long>, account: Account): List<Sneaker>

    /**
     * a method to find the [Sneaker] by the filter.
     *
     * @param accountId the account unique identifier.
     * @param filter the parameter to find the [Sneaker] by brand or color.
     * @param pageable the [Pageable] instance.
     * @return the [Page] of [Sneaker].
     */
    @Query(
        value = """
        FROM Sneaker s
        WHERE s.account.id = :accountId
        AND (:filter IS NULL OR s.brand = :filter)
        OR (:filter IS NULL OR s.color = :filter)
    """
    )
    fun findAllByFilter(accountId: Long, filter: String?, pageable: Pageable): Page<Sneaker>
}
