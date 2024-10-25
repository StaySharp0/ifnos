package com.ifnos.frame.auth.domain.keycloak

import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.PORTAL_CLI_NAME
import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.ROLES
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.*
import org.springframework.stereotype.Service

@Service
class KeycloakAdminService(
    private val keycloakBuilder: KeycloakBuilder,
    private val repo: KeycloakRepo,
) {

    fun createRealm(realmName: String) {
        keycloakBuilder.build().use { admin ->
            admin.realms().create(RealmRepresentation().apply {
                realm = realmName
                isEnabled = true
            })

            admin.realm(realmName).also { realm ->
                // 기본 clientScope Optional 처리
                realm.defaultDefaultClientScopes.forEach {
                    realm.removeDefaultDefaultClientScope(it.id)
                    realm.addDefaultOptionalClientScope(it.id)
                }

                // realmId 주입 관련 클라이언트 스코프 및 프로토콜 매퍼 생성
                realm.clientScopes()
                    .create(ClientScopeRepresentation().apply {
                        name = "realm_id"
                        protocol = "openid-connect"
                        protocolMappers = listOf(
                            ProtocolMapperRepresentation().apply {
                                name = "realm_id_mapper"
                                protocol = "openid-connect"
                                protocolMapper = "oidc-hardcoded-claim-mapper"
                                config = mapOf(
                                    "claim.name" to "realm_id",
                                    "claim.value" to realm.toRepresentation().id,
                                    "jsonType.label" to "String",
                                    "id.token.claim" to "true",
                                    "access.token.claim" to "true",
                                    "userinfo.token.claim" to "true"
                                )
                            }
                        )
                    })

                // 해당 클라이언트 스코프 Default 설정
                val realmIdClientScope = realm.clientScopes().findAll()
                    .firstOrNull { it.name == "realm_id" }
                    ?: throw Exception("Client Scope not found")

                realm.addDefaultDefaultClientScope(realmIdClientScope.id)
                createClient(realmName)
            }
        }
    }

    fun createClient(realmName: String, clientName: String = PORTAL_CLI_NAME) {
        keycloakBuilder.build().use { admin ->
            val realm = admin.realm(realmName)

            // Client 생성
            realm.clients().create(ClientRepresentation().apply {
                clientId = clientName
                isPublicClient = true
                isDirectAccessGrantsEnabled = true
                isServiceAccountsEnabled = true
            })

            val client = realm.clients().findByClientId(clientName).firstOrNull()
                ?: throw IllegalArgumentException("Client not found: $clientName")
            val clientResource = realm.clients().get(client.id)

            // Client Role 생성
            val clientRolesResource = clientResource.roles()
            ROLES.forEach { roleName ->
                clientRolesResource.create(RoleRepresentation().apply {
                    clientRole = true
                    name = roleName
                })
            }

            // Client 그룹 생성
            val clientGroupName = client.id
            realm.groups()
                .add(GroupRepresentation().apply {
                    name = clientGroupName
                    attributes = mapOf("clientName" to listOf(clientName))
                })

            val clientGroupId = realm.groups().groups().find { it.name == clientGroupName }?.id
                ?: throw IllegalStateException("Group '$clientGroupName' was not created successfully.")

            // Client/Role 그룹 생성
            val clientRoles = clientRolesResource.list()
            clientRoles.forEach { role ->
                realm.groups().group(clientGroupId)
                    .subGroup(GroupRepresentation().apply { name = role.name })
            }

            // Client/Role 그룹 ClientRole 매핑
            // https://github.com/keycloak/keycloak/issues/20445
            val roleGroups = realm.groups().group(clientGroupId).getSubGroups(0, ROLES.size, true)
            clientRoles.forEach { role ->
                roleGroups.find { it.name == role.name }?.run {
                    realm.groups().group(id)
                        .roles()
                        .clientLevel(client.id)
                        .add(listOf(role))
                }
            }

            // ClientUUID, Client/Role 목록 주입 프로토콜 매퍼 적용
            clientResource.update(clientResource.toRepresentation().apply {
                val clientUUID = id
                protocolMappers = protocolMappers.orEmpty().toMutableList().apply {
                    // 로그인한 ClientUUID 주입
                    add(ProtocolMapperRepresentation().apply {
                        name = "client_id_mapper"
                        protocol = "openid-connect"
                        protocolMapper = "oidc-hardcoded-claim-mapper"
                        config = mapOf(
                            "claim.name" to "client_id",
                            "claim.value" to clientUUID,
                            "jsonType.label" to "String",
                            "id.token.claim" to "true",
                            "access.token.claim" to "true",
                            "userinfo.token.claim" to "true"
                        )
                    })
                    // Client/Role 목록 주입
                    add(ProtocolMapperRepresentation().apply {
                        name = "group-membership-mapper"
                        protocol = "openid-connect"
                        protocolMapper = "oidc-group-membership-mapper"
                        config = mapOf(
                            "claim.name" to "groups",
                            "full.path" to "true",
                            "jsonType.label" to "String",
                            "id.token.claim" to "true",
                            "access.token.claim" to "true",
                            "userinfo.token.claim" to "true"
                        )
                    })
                }
            })
        }
    }
}

