package com.ifnos.frame.auth.domain.keycloak

import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.PORTAL_CLI_NAME
import org.keycloak.admin.client.resource.RealmResource
import org.springframework.stereotype.Service

@Service
class KeycloakRoleService(private val repo: KeycloakRepo) {

    fun assignClientRoleToUser(
        realm: RealmResource,
        userId: String,
        roleName: String,
        clientId: String = PORTAL_CLI_NAME,
    ) {
        val client = repo.findClientByClientId(realm, clientId)
        val clientRole = repo.findClientRoleById(realm, client.id)
            .get(roleName).toRepresentation()

        realm.users().get(userId)
            .roles()
            .clientLevel(client.id)
            .add(listOf(clientRole))
    }
}
