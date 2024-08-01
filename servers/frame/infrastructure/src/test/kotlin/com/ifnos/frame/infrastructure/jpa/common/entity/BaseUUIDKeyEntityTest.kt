package com.ifnos.frame.infrastructure.jpa.common.entity

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.yaml"])
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseUUIDKeyEntityTest @Autowired constructor(
    val em: TestEntityManager,
    val parentRepo: ParentRepo,
    val childRepo: ChildRepo,
) {
    @Test
    fun `Test @CreatedDate`() {
        var entity = ParentEntityImpl()
        assertEquals(entity.createdAt, LocalDateTime.MIN)
        assertEquals(entity.updatedAt, LocalDateTime.MIN)

        entity = em.persistFlushFind(entity)

        assertNotEquals(entity.createdAt, LocalDateTime.MIN)
        assertNotEquals(entity.updatedAt, LocalDateTime.MIN)
        assertEquals(entity.createdAt, entity.updatedAt)
    }

    @Test
    fun `Test @LastModifiedDate`() {
        val entity = em.persistFlushFind(ParentEntityImpl())

        entity.text = "change `updateAt`"
        em.flush()

        assertNotEquals(entity.createdAt, entity.updatedAt)
    }

    @Test
    fun `Test detached entity`() {
        val detachedEntity = em.persistFlushFind(ParentEntityImpl())
        assertNotNull(detachedEntity)

        parentRepo.findById(detachedEntity.id).get().run {
            assertNotNull(this)
            em.detach(detachedEntity)

            /*  this, post~ 는 동일 객체이므로 flush를 해도 반영되지 않는다. */
            assertEquals(this, detachedEntity)
            detachedEntity.text = "change `managedEntity.updateAt`"
            this.text = "change `managedEntity.updateAt`"
            em.flush()

            assertEquals(detachedEntity.createdAt, detachedEntity.updatedAt)
            assertEquals(this.createdAt, this.updatedAt)
        }
    }

    @Test
    fun `Test save & delete entity`() {
        val entity = ParentEntityImpl()

        parentRepo.save(entity)
        parentRepo.flush()

        parentRepo.delete(entity)
        parentRepo.flush()

        parentRepo.findById(entity.id).run {
            assertNotNull(this)
        }
    }

    @Test
    fun `Test same lazy-load & loaded entity`() {
        val parent = ParentEntityImpl()
        val child = ChildEntityImpl(parent)

        em.persist(parent)
        em.persist(child)
        em.flush()
        em.clear()

        /* 동기화 확인 */
        assertTrue(parent.children.contains(child))

        childRepo.findById(child.id).get().run {
            /* lazy loading 확인 */
            assertTrue(this.parent is HibernateProxy)
            /* lazy loading 해서 id 같은지 비교 확인 */
            assertEquals(this.parent, parent)
        }

        parentRepo.findById(parent.id).get().run {
            /* 연관관계 동작 확인 */
            assertTrue(this.children.contains(child))
        }
    }

    @Test
    fun `Test encapsulation mutable property`() {
        val parent = ParentEntityImpl()
        val child = ChildEntityImpl(parent)

        em.persist(parent)
        em.persist(child)
        em.flush()
        em.clear()

        parentRepo.findById(parent.id).get().run {
            val pastChildren = this.children
            assertEquals(pastChildren.size, 1)

            val newChild = ChildEntityImpl(this)
            this.addChild(newChild)

            /* children은 조회한 시점에 toList()로 복제되어 크기가 1 */
            assertEquals(pastChildren.size, 1)
            assertEquals(this.children.size, 2)
        }

    }

    @SpringBootApplication
    @EnableJpaAuditing
    class TestApplication
}

@Entity
class ParentEntityImpl : BaseUUIDKeyEntity() {
    @Column
    var text: String = ""

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "parent")
    protected val mutableChildren: MutableList<ChildEntityImpl> = mutableListOf()
    val children get(): List<ChildEntityImpl> = mutableChildren.toList()

    fun addChild(child: ChildEntityImpl) {
        if (child !in mutableChildren)
            mutableChildren.add(child)
    }

}

@Entity
class ChildEntityImpl(
    parent: ParentEntityImpl
) : BaseUUIDKeyEntity() {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    var parent: ParentEntityImpl = parent
        protected set

    init {
        parent.addChild(this)
    }
}

interface ParentRepo : JpaRepository<ParentEntityImpl, UUID>
interface ChildRepo : JpaRepository<ChildEntityImpl, UUID>
