package com.sparta.memo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass  // 이 에노테이션이 붙은 클래스를 상속 받는 경우, 상위 클래스의 필드를 하위 클래스의 필드로 인식하게 된다.
@EntityListeners(AuditingEntityListener.class) // 해당 클래스에 Auditing 기능 추가
public abstract class Timestamped {

    @CreatedDate // Entity 객체가 생성될 때 시간값 자동 저장
    @Column(updatable = false) // updatable = false : 업데이트 불가
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate // 조회한 객체 변경 시 시간값 자동 저장
    @Column
    @Temporal(TemporalType.TIMESTAMP) // @Temporal : Date, Calender 사용 시 // TIMESTAMP : 2024-08-21 17:53:25.3333
    private LocalDateTime modifiedAt;
}


/*
JPA Auditing: 시간에 대해서 자동으로 값을 넣어주는 기능, Spring Data JPA에서 제공.

 */