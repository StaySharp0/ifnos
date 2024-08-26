package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.springframework.stereotype.Service

@Service
class KeycloakAdminService(
    private val admin: Keycloak,
    private val roleService: KeycloakRoleService,
    private val repo: KeycloakRepo,
) {

    fun createRealm(realmName: String) {
        admin.realms().create(RealmRepresentation().apply {
            id = realmName
            realm = realmName
            isEnabled = true
        })

        admin.realm(realmName).run {
            // TODO: app이 만들어질 때마다 생성되어야할듯...
            createClient(this)
            setALlOptionalClientScope(this)
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

    private fun setALlOptionalClientScope(realm: RealmResource, clientId: String = "portal-cli") {
        val client = repo.findClientResourceByClientId(realm, clientId)

        client.defaultClientScopes.forEach { scope ->
            client.removeDefaultClientScope(scope.id)
            client.addOptionalClientScope(scope.id)
        }
    }

    companion object {
        const val PORTAL_CLI_ID = "portal-cli"
    }
}

