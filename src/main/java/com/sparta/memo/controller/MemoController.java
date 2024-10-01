package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MemoController {

    // DB
    private final Map<Long, Memo> memoList = new HashMap<>();

    // 메모 생성하기 , 클라이언트의 입력을 받아 메모로 응답함.
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity , requestDto 에서 이름과 내용을 받아 Memo 로 넘김.
        Memo memo = new Memo(requestDto);

        // Memo 에 id 부여
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB에 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }

    // 자바 언어의 문법 (stream , lamda) : 개념 파악 - 기본 지식을 쌓고 예시, 코드를 통해 학습하면 된다.
    // deep L : 언어 번역
    // 깊은 지식은 책을 통해서 쌓는 게 낫다.

    // 메모 조회하기
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map to List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();
        //  MemoResponseDto::new 를 MemoResponseDto -> new MemoResponseDto() 로 바꿀 때 파라미터를 어떻게 해야함? MemoResponseDto
        // MemoResponseDto::new 의 의미 : map 에서 꺼낸 값을 MemoResponseDto의 생성자에 넣겠다.
        // 위와 같은 표현은 생성자 파라미터가 1개일때만 쓸 수 있음
        return responseList;
    }

    // 메모 변경하기
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        if (memoList.containsKey(id)) {
            // 해당 메모 가져오기
            Memo memo = memoList.get(id);
            // 메모 수정
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

        // 메모 삭제하기
    @DeleteMapping("/memos/")
    public Long updateMemo(@RequestBody Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) {
            // 해당 메모 삭제하기
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}