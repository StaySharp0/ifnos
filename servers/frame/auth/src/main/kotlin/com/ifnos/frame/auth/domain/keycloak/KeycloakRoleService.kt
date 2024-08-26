package com.ifnos.frame.auth.domain.keycloak

import com.ifnos.frame.auth.domain.keycloak.KeycloakAdminService.Companion.PORTAL_CLI_ID
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.RoleRepresentation
import org.springframework.stereotype.Service

@Service
class KeycloakRoleService(private val repo: KeycloakRepo) {

    fun createClientRoles(
        realm: RealmResource,
        clientId: String = "portal-cli",
        roles: List<String> = listOf("super-admin", "app-admin", "user"),
    ) {
        val clientRoles = repo.findClientRoleByClientId(realm, clientId)
        roles.forEach { roleName ->
            clientRoles.create(RoleRepresentation().apply {
                clientRole = true
                name = roleName
            })
        }
    }

    fun assignClientRoleToUser(
        realm: RealmResource,
        userId: String,
        roleName: String,
        clientId: String = PORTAL_CLI_ID,
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
