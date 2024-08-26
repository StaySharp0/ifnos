package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.springframework.stereotype.Service

@Service
class KeycloakAdminService(
    private val keycloak: Keycloak,
    private val roleService: KeycloakRoleService,
) {

    fun createRealm(realmName: String) {
        keycloak.realms().create(RealmRepresentation().apply {
            id = realmName
            realm = realmName
            isEnabled = true
        })

        // TODO: app이 만들어질 때마다 생성되어야할듯...
        keycloak.realm(realmName).run {
            createClient(this)
            roleService.createClientRoles(this)
        }
    }

    fun createClient(realm: RealmResource, clientId: String = "portal-cli") {
        realm.clients().create(ClientRepresentation().apply {
            this.clientId = clientId
            isPublicClient = true
            isDirectAccessGrantsEnabled = true
            isServiceAccountsEnabled = true
        })
    }

    companion object {
        const val PORTAL_CLI_ID = "portal-cli"
    }
}