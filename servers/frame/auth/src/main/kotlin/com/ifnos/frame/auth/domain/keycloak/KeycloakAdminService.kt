package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.*
import org.springframework.stereotype.Service

@Service
class KeycloakAdminService(
    private val keycloak: Keycloak,
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
            createRoles(this)
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

    private fun createRoles(realm: RealmResource, roles: List<String> = listOf("super-admin", "app-admin", "user")) {
        roles.forEach { roleName ->
            realm.roles()
                .create(RoleRepresentation().apply {
                    name = roleName
                })
        }
    }

    fun createUser(realmName: String, email: String, name: String, password: String) {
        val realm = keycloak.realm(realmName)

        realm.users()
            .create(UserRepresentation().apply {
                username = email
                this.email = email
                isEnabled = true
                /* 필수 요소가 아니지만 설정되지 않으면 로그인이 되지않는 문제가 있음
                 * NOTE: https://stackoverflow.com/questions/42524153/keycloak-not-returning-access-token-if-update-password-action-selected */
                firstName = name
                lastName = name
            }).run {
                val userId = location.path.substringAfterLast("/")

                setUserPassword(realm, userId, password)
                // TODO: app에 조인할때 권한 줄것
                assignRoleToUser(realm, userId, "user")
            }
    }

    private fun setUserPassword(realm: RealmResource, userId: String, password: String) {
        realm.users().get(userId)
            .resetPassword(CredentialRepresentation().apply {
                type = CredentialRepresentation.PASSWORD
                value = password
                isTemporary = false
            })
    }

    private fun assignRoleToUser(realm: RealmResource, userId: String, roleName: String) {
        val roleRepresentation = realm.roles().get(roleName).toRepresentation()

        realm.users().get(userId)
            .roles().realmLevel()
            .add(listOf(roleRepresentation))
    }
}