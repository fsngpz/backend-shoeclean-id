package id.shoeclean.engine.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * The class for controller exception handler.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@ControllerAdvice
class ControllerExceptionHandler {

    /**
     * Handle general exception.
     *
     * @param e the [Exception].
     * @return the [ErrorResponse] with [HttpStatus.INTERNAL_SERVER_ERROR].
     */
    @ExceptionHandler
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        // -- setup the instance of error response --
        val errorResponse = ErrorResponse(
            type = e.javaClass.simpleName,
            message = e.message ?: e.stackTraceToString()
        )
        // -- return as response entity --
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * Handle the Illegal Argument Exception.
     *
     * @param e the [IllegalArgumentException].
     * @return the [ErrorResponse] with [HttpStatus.BAD_REQUEST].
     */
    @ExceptionHandler
    fun handleIllegalArgumentExceptions(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        // -- setup the instance of error response --
        val errorResponse = ErrorResponse(
            type = e.javaClass.simpleName,
            message = e.message ?: e.stackTraceToString()
        )
        // -- return as response entity --
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * Handle the No Such Element Exception.
     *
     * @param e the [NoSuchElementException].
     * @return the [ErrorResponse] with [HttpStatus.NOT_FOUND].
     */
    @ExceptionHandler
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<ErrorResponse> {
        // -- setup the instance of error response --
        val errorResponse = ErrorResponse(
            type = e.javaClass.simpleName,
            message = e.message ?: e.stackTraceToString()
        )
        // -- return as response entity --
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handle the Duplicate Email Exception.
     *
     * @param e the [DuplicateEmailException].
     * @return the [ErrorResponse] with [HttpStatus.CONFLICT].
     */
    @ExceptionHandler
    fun handleDuplicateEmailException(e: DuplicateEmailException): ResponseEntity<ErrorResponse> {
        // -- setup the instance of error response --
        val errorResponse = ErrorResponse(
            type = e.javaClass.simpleName,
            message = e.message ?: e.stackTraceToString()
        )
        // -- return as response entity --
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
}
