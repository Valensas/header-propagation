package com.valensas.headerpropagation.interceptor

import com.valensas.headerpropagation.config.ThreadLocalHeaderStore
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class WebHeaderExtractorFilter(
    private val headerPropagationProperties: HeaderPropagationProperties
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val propagatedHeaders: MutableMap<String, String> = ThreadLocalHeaderStore.headers.toMutableMap()

        headerPropagationProperties.headers.forEach { header ->
            request.getHeader(header)?.let {
                propagatedHeaders[header] = it
            }
        }

        ThreadLocalHeaderStore.headers = propagatedHeaders

        filterChain.doFilter(request, response)
    }
}
