package com.sparta.memo.entity;

import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// entity : 데이터 베이스와 소통 담당
@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id;
    private String username;
    private String contents;

    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
