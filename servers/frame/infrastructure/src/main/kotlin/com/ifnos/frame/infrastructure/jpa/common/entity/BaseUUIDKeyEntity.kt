package com.ifnos.frame.infrastructure.jpa.common.entity

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseUUIDKeyEntity : Persistable<UUID> {

    @Id
    @Column(
        columnDefinition = "uuid", /* NOTE: DB 호환성이 필요한 경우 BINARY(16) */
        nullable = false, updatable = false
    )
    private val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.MIN
        protected set

    /* Persistable */
    override fun getId(): UUID = id
    override fun isNew() = createdAt == LocalDateTime.MIN

    override fun equals(other: Any?): Boolean = when {
        other == null -> false
        other !is HibernateProxy && this::class != other::class -> false
        other is HibernateProxy -> id == other.hibernateLazyInitializer.identifier
        else -> id == (other as BaseUUIDKeyEntity).id
    }

    override fun hashCode() = Objects.hashCode(id)
}
