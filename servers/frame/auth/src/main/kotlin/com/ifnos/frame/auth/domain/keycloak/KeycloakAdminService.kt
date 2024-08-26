package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.ProtocolMapperRepresentation
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
            addClientIdToToken(this)
        }
    }

    fun createClient(realm: RealmResource, clientId: String = PORTAL_CLI_ID) {
        realm.clients().create(ClientRepresentation().apply {
            this.clientId = clientId
            isPublicClient = true
            isDirectAccessGrantsEnabled = true
            isServiceAccountsEnabled = true
        })
    }

    private fun setALlOptionalClientScope(realm: RealmResource, clientId: String = PORTAL_CLI_ID) {
        val client = repo.findClientResourceByClientId(realm, clientId)

        client.defaultClientScopes.forEach { scope ->
            client.removeDefaultClientScope(scope.id)
            client.addOptionalClientScope(scope.id)
        }
    }

    private fun addClientIdToToken(realm: RealmResource, clientId: String = PORTAL_CLI_ID) {
        val client = repo.findClientByClientId(realm, clientId)
        val clientResource = repo.findClientResourceByClientId(realm, clientId)
        val clientRepresentation = clientResource.toRepresentation()

        // Protocol Mapper 생성
        val mapper = ProtocolMapperRepresentation().apply {
            name = "client_resource_id_mapper"
            protocol = "openid-connect"
            protocolMapper = "oidc-hardcoded-claim-mapper"
            config = mapOf(
                "claim.name" to "client_id",
                "claim.value" to client.id,
                "jsonType.label" to "String",
                "id.token.claim" to "true",
                "access.token.claim" to "true",
                "userinfo.token.claim" to "true"
            )
        }

        val mappers = clientRepresentation.protocolMappers.orEmpty().toMutableList()
        mappers.add(mapper)
        clientRepresentation.protocolMappers = mappers

        clientResource.update(clientRepresentation)
    }

    companion object {
        const val PORTAL_CLI_ID = "portal-cli"
    }
}

