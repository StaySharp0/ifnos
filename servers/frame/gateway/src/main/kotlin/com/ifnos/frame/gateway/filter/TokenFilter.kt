package com.ifnos.frame.gateway.filter

import com.ifnos.frame.gateway.auth.TokenProcessor
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class TokenFilter(
    private val tokenProcessor: TokenProcessor,
) : GatewayFilterFactory<TokenFilter.Config> {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val token = extractToken(exchange)

            when {
                token == null -> unauthorized(exchange)
                tokenProcessor.isValidToken(token) -> authorized(exchange, chain, token)
                tokenProcessor.isTokenExpired(token) -> tokenProcessor.refreshAccessToken(token)
                    ?.let { newAccessToken ->
                        authorized(exchange, chain, token) {
                            exchange.response.headers
                                .add(HttpHeaders.AUTHORIZATION, "Bearer $newAccessToken")
                        }
                    } ?: unauthorized(exchange)

                else -> unauthorized(exchange)
            }
        }
    }

    private fun extractToken(exchange: ServerWebExchange): String? =
        exchange.request.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.substringAfter("Bearer ")

    private fun authorized(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
        token: String,
        cb: (() -> Unit)? = null,
    ): Mono<Void> {
        exchange.mutate().request {
            it.headers { headers ->
                headers.remove(HttpHeaders.AUTHORIZATION)

                headers.add("X-Realm-ID", tokenProcessor.getRealmId(token))
            }
        }

        cb?.invoke()

        return chain.filter(exchange)
    }

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> =
        exchange.response.run {
            statusCode = HttpStatus.UNAUTHORIZED
            setComplete()
        }

    class Config
}
