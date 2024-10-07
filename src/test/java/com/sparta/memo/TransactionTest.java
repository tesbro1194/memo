package com.sparta.memo;

import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TransactionTest {
    // SpringBoot 에서 생성하는 EntityManager 사용하기 위해 붙이는 에노테이션
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemoRepository memoRepository;

    @Test
    @Transactional
    @Rollback(value = false) // 테스트 코드에서 @Transactional 를 사용하면 테스트가 완료된 후 롤백하기 때문에 false 옵션 추가
    @DisplayName("메모 생성 성공")
    void test1() {
        Memo memo = new Memo();
        memo.setUsername("Robbert");
        memo.setContents("@Transactional 테스트 중!");

        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
    }

    @Test
    @DisplayName("메모 생성 실패")
    @Disabled  //  아래 메서드를 더 이상 사용하지 않겠다
    void test2() {
        Memo memo = new Memo();
        memo.setUsername("Robbie");
        memo.setContents("@Transactional 테스트 중!");

        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
    }

    /*- 스프링 컨테이너 환경에서는 영속성 컨텍스트와 트랜잭션의 생명주기가 일치합니다.
      - 트랜잭션이 유지되는 동안은 영속성 컨텍스트도 계속 유지가 되기 때문에 영속성 컨텍스트의 기능을 사용할 수 있습니다.*/
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("트랜잭션 전파 테스트")
    @Disabled
    void test3() {  // 부모 메서드
//        memoRepository.createMemo(em);
        System.out.println("테스트 test3 메서드 종료");
    }
}

/*
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
						...

		@Transactional
		@Override
		public <S extends T> S save(S entity) {

			Assert.notNull(entity, "Entity must not be null");

			if (entityInformation.isNew(entity)) {
				em.persist(entity);
				return entity;
			} else {
				return em.merge(entity);
			}
		}
						...
}


@Transactional 애너테이션을 클래스나 메서드에 추가하면 쉽게 트랜잭션 개념을 적용할 수 있습니다.
- 메서드가 호출되면, 해당 메서드 내에서 수행되는 모든 DB 연산 내용은 하나의 트랜잭션으로 묶입니다.
- 이때, 해당 메서드가 정상적으로 수행되면 트랜잭션을 커밋하고, 예외가 발생하면 롤백합니다.
- 클래스에 선언한 @Transactional은 해당 클래스 내부의 모든 메서드에 트랜잭션 기능을 부여합니다.
- 이때 save 메서드는 @Transactional 애너테이션이 추가되어있기 때문에
`readOnly = true` 옵션인 @Transactional을 덮어쓰게 되어 `readOnly = false` 옵션으로 적용됩니다.*/