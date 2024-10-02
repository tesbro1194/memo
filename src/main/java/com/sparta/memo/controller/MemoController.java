package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {
/*
    JDBC : Java Database Connectivity로 DB에 접근할 수 있도록 Java에서 제공하는 API
    JDBC에 연결해야하는 DB의 JDBC 드라이버를 제공하면 DB 연결 로직을 변경할 필요없이 DB 변경이 가능함.
    DB 회사들은 자신들의 DB에 맞도록 JDBC 인터페이스를 구현한 후 라이브러리로 제공하며 이를 JDBC 드라이버라고 함.
    따라서 MySQL 드라이버를 사용해 DB에 연결을 하다 PostgreSQL 서버로 변경이 필요할 때 드라이버만 교체하면 손쉽게 DB 변경이 가능함.

    JdbcTemplate : DB에 연결하기 위해 필요한 커넥션 연결, statement 준비 및 실행, 커넥션 종료 등의 반복적이고 중복되는 작업들을 처리함.
    사용 방법 :
    1. application.properties에 DB에 접근하기 위한 정보를 작성합니다.
spring.datasource.url=jdbc:mysql://localhost:3306/memo
spring.datasource.username=root
spring.datasource.password={비밀번호}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    2. build.gradle에 JDBC 라이브러리와 MySQL을 등록합니다.
implementation 'mysql:mysql-connector-java:8.0.28'
implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'

    3. DB연결이 필요한 곳에서 JdbcTemplate을 주입받아와 사용합니다.
public MemoController(JdbcTemplate jdbcTemplate) {
this.jdbcTemplate = jdbcTemplate;
}
*/


    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
        // jdbcTemplate 의 update 메서드 : update, insert, delete 문
        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        // query 메서드 : select 문.
        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                // get 뒤에 타입. ()에 컬럼명.
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if(memo != null) {
            // memo 내용 수정
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if(memo != null) {
            // memo 삭제
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }
}