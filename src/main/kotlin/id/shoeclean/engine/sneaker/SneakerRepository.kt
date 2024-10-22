package id.shoeclean.engine.sneaker

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Sneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
interface SneakerRepository : JpaRepository<Sneaker, Long> {
}
