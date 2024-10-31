package id.shoeclean.engine.transaction

/**
 * an extension class to map the [Transaction] to [TransactionResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-01
 */
fun Transaction.toResponse(): TransactionResponse {
    val uscId = this.order.uscId
    // -- validate the uscId --
    requireNotNull(uscId) {
        throw IllegalArgumentException("the uscId is null")
    }
    // -- map the Transaction to TransactionResponse --
    return TransactionResponse(
        uscId = uscId,
        serviceType = this.order.catalog.serviceType,
        amount = this.totalAmount,
        method = this.method,
        status = this.status,
        createdAt = this.createdAt
    )
}
