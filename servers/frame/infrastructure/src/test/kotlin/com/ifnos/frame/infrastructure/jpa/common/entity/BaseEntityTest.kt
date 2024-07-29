package com.ifnos.frame.infrastructure.jpa.common.entity

import jakarta.persistence.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime


@Entity
@EntityListeners(value = [AuditingEntityListener::class])
class BaseEntityImpl(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column
    var text: String = ""
) : BaseEntity()

interface TestRepo : JpaRepository<BaseEntityImpl, Long>


@TestPropertySource(locations = ["classpath:application-test.yaml"])
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseEntityTest {
    @Autowired
    lateinit var em: TestEntityManager

    @Autowired
    lateinit var repo: TestRepo

    @Test
    fun `Test @CreatedDate`() {
        var entity = BaseEntityImpl()
        assertEquals(entity.createdAt, LocalDateTime.MIN)
        assertEquals(entity.updatedAt, LocalDateTime.MIN)

        entity = em.persistFlushFind(entity)

        assertNotEquals(entity.createdAt, LocalDateTime.MIN)
        assertNotEquals(entity.updatedAt, LocalDateTime.MIN)
        assertEquals(entity.createdAt, entity.updatedAt)
    }

    @Test
    fun `Test @LastModifiedDate`() {
        val entity = em.persistFlushFind(BaseEntityImpl())

        entity.text = "change `updateAt`"
        em.flush()

        assertNotEquals(entity.createdAt, entity.updatedAt)
    }

    @Test
    fun `Test detached entity`() {
        val preDetachedEntity = em.persistFlushFind(BaseEntityImpl())
        assertNotNull(preDetachedEntity.id)

        em.detach(preDetachedEntity)

        preDetachedEntity.id?.let {
            val managedEntity = repo.findById(it).get()
            assertNotNull(managedEntity)

            /* 해당 객체가 캐시 아웃된 이후 조회했기 때문에 새로운 객체 반환 */
            assertNotEquals(managedEntity, preDetachedEntity)
        }

        val postDetachedEntity = em.persistFlushFind(BaseEntityImpl())
        assertNotNull(postDetachedEntity.id)

        postDetachedEntity.id?.let {
            /*  post~, managed~는 같은 객체  */
            val managedEntity = repo.findById(it).get()
            assertNotNull(managedEntity)

            em.detach(postDetachedEntity)
            assertEquals(managedEntity, postDetachedEntity)

            managedEntity.text = "change `managedEntity.updateAt`"
            em.flush()

            /* 준영속 상태이기 때문에 커밋되어도 updatedAt이 갱신되지 않는다. */
            assertEquals(managedEntity.createdAt, managedEntity.updatedAt)
        }
    }

    @SpringBootApplication
    @EnableJpaAuditing
    @ComponentScan(basePackages = ["com.ifnos.frame.infrastructure.jpa.common.entity"])
    class TestApplication
}
