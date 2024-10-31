package id.shoeclean.engine.orders

import id.shoeclean.engine.addresses.toResponse
import id.shoeclean.engine.exceptions.VoucherNotSufficeOrderQtyException
import id.shoeclean.engine.exceptions.VoucherNotSufficeOrderSubtotalException
import id.shoeclean.engine.vouchers.Voucher
import id.shoeclean.engine.vouchers.VoucherType
import java.math.BigDecimal

/**
 * an extension function to convert the [Order] to [SubmitOrderResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
fun Order.toSubmitOrderResponse(): SubmitOrderResponse {
    val uscId = this.uscId
    // -- validate the field orderId --
    requireNotNull(uscId) { "USC ID must not be null" }
    // -- return the instance --
    return SubmitOrderResponse(uscId)
}

/**
 * an extension function to convert the [Order] to [OrderDetailResponse]. This function also will calculate
 * the voucher of given [Order] instance.
 *
 * @return the [OrderDetailResponse].
 */
fun Order.toOrderDetailResponse(voucher: Voucher?): OrderDetailResponse {
    val uscId = this.uscId
    // -- validate the field orderId --
    requireNotNull(uscId) { "USC ID must not be null" }

    val totalPairs = this.totalPairs
    val price = this.catalog.price

    val discount = this.getDiscount(voucher)
    // get the subtotal from price * totalPairs
    val subtotal = price.multiply(BigDecimal(totalPairs))
    // get the totalAmount from subtotal - discount
    val totalAmount = subtotal.subtract(discount)

    return OrderDetailResponse(
        uscId = uscId,
        address = this.address.toResponse(),
        totalPairs = totalPairs,
        serviceType = this.catalog.serviceType,
        price = price,
        discount = discount,
        deliveryFee = BigDecimal.ZERO, // TODO: now the delivery hardcoded to be ZERO
        subtotal = subtotal,
        totalAmount = totalAmount
    )
}

/**
 * a function to get the discount from [Order] instance. Basically this function will try to calculate the discount
 * based on voucher type.
 *
 * @return the [BigDecimal].
 */
fun Order.getDiscount(voucher: Voucher?): BigDecimal {
    val price = this.catalog.price
    val totalPairs = BigDecimal(this.totalPairs)
    requireNotNull(voucher) {
        return BigDecimal.ZERO
    }
    return if (voucher.type == VoucherType.FREE_PAIR) {
        validateTotalPairsAndVoucher(this.totalPairs, voucher.amount)
        // -- for free pair it will calculate the price with the voucher amount --
        price.multiply(voucher.amount)
    } else {
        val subtotal = price.multiply(totalPairs)
        validateSubtotalAndVoucher(subtotal, voucher.amount)
        // -- for discount, return the amount --
        voucher.amount
    }
}

/**
 * a function to validate the total pairs with voucher amount.
 *
 * @param totalPairs the total pairs.
 * @param voucherAmount the voucher amount.
 */
fun validateTotalPairsAndVoucher(totalPairs: Int, voucherAmount: BigDecimal) {
    val isSuffice = BigDecimal(totalPairs) >= voucherAmount
    if (!isSuffice) {
        throw VoucherNotSufficeOrderQtyException("Total pair can't be less than voucher amount")
    }
}

/**
 * function to validate the subtotal with voucher amount.
 *
 * @param subtotal the subtotal of order.
 * @param voucherAmount the voucher amount
 */
fun validateSubtotalAndVoucher(subtotal: BigDecimal, voucherAmount: BigDecimal) {
    val isSuffice = subtotal >= voucherAmount
    if (!isSuffice) {
        throw VoucherNotSufficeOrderSubtotalException("sub total can't be less than voucher amount")
    }
}
