package no.nav.yrkesskade.skadeforklaring.security

import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import org.springframework.stereotype.Service

@Service
class TokenService (
    private val clientConfigurationProperties: ClientConfigurationProperties,
    private val oAuth2AccessTokenService: OAuth2AccessTokenService
) {

    fun getAppAccessTokenWithScope(clientName: String): String {
        val clientProperties = clientConfigurationProperties.registration[clientName]
        val response = oAuth2AccessTokenService.getAccessToken(clientProperties)
        return response.accessToken
    }
}