package com.ifnos.frame.auth.domain.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service

@Service
class KeycloakUserService(
    private val keycloak: Keycloak,
    private val roleService: KeycloakRoleService,
) {
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
                roleService.assignClientRoleToUser(realm, userId, "user")
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
}