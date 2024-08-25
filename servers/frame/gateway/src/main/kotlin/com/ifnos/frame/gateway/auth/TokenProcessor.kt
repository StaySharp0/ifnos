package com.ifnos.frame.gateway.auth

import com.ifnos.frame.gateway.client.AuthClient
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.text.ParseException
import java.util.*

@Component
class TokenProcessor(
    @Lazy private val authClient: AuthClient,
) {

    fun isValidToken(token: String): Boolean {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val jwkSet = JWKSet.parse(authClient.getJwkSet())

            jwkSet.getKeyByKeyId(signedJWT.header.keyID)?.let { jwk ->
                val verifier = RSASSAVerifier(jwk.toRSAKey().toRSAPublicKey())

                signedJWT.verify(verifier) && !isTokenExpired(token)
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val claims = signedJWT.jwtClaimsSet

            claims.expirationTime.before(Date())
        } catch (e: ParseException) {
            true
        }
    }

    fun getRealmId(token: String): String =
        SignedJWT.parse(token)
            .jwtClaimsSet
            .getStringClaim("realmId")

    fun refreshAccessToken(token: String) = try {
        SignedJWT.parse(token)
            .jwtClaimsSet
            .getStringClaim("refresh_token").let { refreshToken ->
                authClient.refreshToken(refreshToken)
            }
    } catch (e: Exception) {
        null
    }
}
