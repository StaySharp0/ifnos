package com.ifnos.frame.auth.domain.keycloak

import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.PORTAL_CLI_NAME
import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.ROLES
import org.keycloak.admin.client.resource.RealmResource
import org.springframework.stereotype.Service

@Service
class KeycloakGroupService {

    fun joinRoleGroup(
        realm: RealmResource,
        userId: String,
        roleName: String = "user",
        clientName: String = PORTAL_CLI_NAME,
    ) {
        val client = realm.clients().findByClientId(clientName).firstOrNull()
            ?: throw IllegalArgumentException("Client not found: $clientName")

        val clientGroupName = client.id
        val clientGroupId = realm.groups().groups()
            .find { it.name == clientGroupName }?.id
            ?: throw IllegalStateException("ClientGroup not found: $clientGroupName")

        val roleGroup = realm.groups().group(clientGroupId)
            .getSubGroups(0, ROLES.size, true)
            .find { it.name == roleName }
            ?: throw IllegalStateException("ClientGroup not found: $clientGroupName")

        realm.users().get(userId).joinGroup(roleGroup.id)
    }
}
