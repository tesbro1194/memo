package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class MemoService {
    private final MemoRepository memoRepository;
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }
    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }
    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(MemoResponseDto::new).toList();

    }
    public List<MemoResponseDto> getMemosByKeyword(@RequestParam String keyword) {
        return memoRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream()
                .map( memo -> new MemoResponseDto(memo))
                .toList();
    }
    @Transactional  //jpa에는 변경 감지(Dirty Checking) 기능이 있음. Transactional 에노테이션이 활성화함.
    // 위 에노테이션이 걸린 메서드는 수행된 다음 commit이 이루어짐. 즉 변경 사항(C, U, D) 저장.
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        // memo 수정
        memo.update(requestDto);
        return id;
    }
    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        memoRepository.delete(memo);
        return id;

    }
    private Memo findMemo(Long id) {
        // Optional은 null check를 해야함 -> orElseThrow() : 예외 처리
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }
}
