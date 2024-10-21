package id.shoeclean.engine.configs

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.util.Collections
import java.util.Enumeration

/**
 * The class to add custom header for [HttpServletRequest].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
class CustomHeaderHttpServletRequest(
    reques: HttpServletRequest,
    private val customHeader: Map<String, String>
) : HttpServletRequestWrapper(reques) {

    override fun getHeader(name: String): String? {
        return customHeader[name] ?: super.getHeader(name)
    }

    override fun getHeaderNames(): Enumeration<String> {
        val originalHeaderNames = super.getHeaderNames().toList()
        val customHeaderNames = customHeader.keys
        val allHeaderNames = LinkedHashSet<String>().apply {
            addAll(originalHeaderNames)
            addAll(customHeaderNames)
        }
        return Collections.enumeration(allHeaderNames)
    }

    private fun Enumeration<String>.toList(): List<String> {
        val list = mutableListOf<String>()
        while (this.hasMoreElements()) {
            list.add(this.nextElement())
        }
        return list
    }
}

