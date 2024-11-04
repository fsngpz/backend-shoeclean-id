package id.shoeclean.engine

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The controller for Root. This is to check the healthiness of this application.
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/")
class RootController {

    /**
     * a GET mapping to check the service is alive or not.
     *
     * @return the string.
     */
    @GetMapping
    fun root(): String {
        return "I'm OK!"
    }
}
