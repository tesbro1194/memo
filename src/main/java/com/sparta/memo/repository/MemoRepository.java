package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();


}

/*
Query Methods: 서드 이름으로 SQL을 생성하는 기능
JpaRepository 인터페이스에서 해당 인터페이스와 매핑되어있는 테이블에 요청하고자하는 SQL을 메서드 이름을 사용하여 선언할 수 있습니다.
SimpleJpaRepository 클래스가 생성될 때 위처럼 직접 선언한 JpaRepository 인터페이스의 모든 메서드를 자동으로 구현합니다.
- JpaRepository 인터페이스의 메서드 즉, Query Methods는 개발자가 이미 정의 되어있는 규칙에 맞게 메서드를 선언하면 해당 메서드 이름을 분석하여 SimpleJpaRepository에서 구현이 됩니다.
- 따라서 우리는 인터페이스에 필요한 SQL에 해당하는 메서드 이름 패턴으로 메서드를 선언 하기만 하면 따로 구현하지 않아도 사용할 수 있습니다.

*/