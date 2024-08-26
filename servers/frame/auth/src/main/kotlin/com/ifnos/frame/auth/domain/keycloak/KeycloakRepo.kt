package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.resource.ClientResource
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.RolesResource
import org.springframework.stereotype.Repository

@Repository
class KeycloakRepo {
    fun findClientByClientId(realm: RealmResource, clientId: String) =
        realm.clients().findByClientId(clientId).firstOrNull()
            ?: throw IllegalArgumentException("Client not found: $clientId")

    fun findClientResourceByClientId(realm: RealmResource, clientId: String): ClientResource =
        realm.clients().get(findClientByClientId(realm, clientId).id)

    fun findClientRoleById(realm: RealmResource, id: String): RolesResource =
        realm.clients().get(id).roles()

    fun findClientRoleByClientId(realm: RealmResource, clientId: String): RolesResource =
        findClientRoleById(realm, findClientByClientId(realm, clientId).id)
}
