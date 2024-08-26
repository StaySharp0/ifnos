package com.ifnos.frame.auth.domain.keycloak

import com.ifnos.frame.auth.domain.keycloak.KeycloakAdminService.Companion.PORTAL_CLI_ID
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.springframework.stereotype.Service

@Service
class KeycloakRoleService {

    fun createClientRoles(
        realm: RealmResource,
        clientId: String = "portal-cli",
        roles: List<String> = listOf("super-admin", "app-admin", "user"),
    ) {
        val clientRoles = findClientRoleByClientId(realm, clientId)
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
        val client = findClientByClientId(realm, clientId)
        val clientRole = findClientRoleById(realm, client).get(roleName).toRepresentation()

        realm.users().get(userId)
            .roles()
            .clientLevel(client.id)
            .add(listOf(clientRole))
    }

    private fun findClientByClientId(realm: RealmResource, clientId: String) =
        realm.clients().findByClientId(clientId).firstOrNull()
            ?: throw IllegalArgumentException("Client not found: $clientId")

    private fun findClientRoleById(realm: RealmResource, client: ClientRepresentation) =
        realm.clients().get(client.id).roles()

    private fun findClientRoleByClientId(realm: RealmResource, clientId: String) =
        findClientRoleById(realm, findClientByClientId(realm, clientId))
}