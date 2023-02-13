package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.member.MemberResponse;
import beachcombine.backend.dto.member.MemberUpdateRequest;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse findMemberInfo(long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return findMember.getMemberInfo();
    }


    // 회원 정보 수정
    public void updateMemberInfo(long id, MemberUpdateRequest dto) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (!dto.getNickname().equals(findMember.getNickname())
                && (memberRepository.existsByNickname(dto.getNickname()))
        ) {
            throw new CustomException(ErrorCode.EXIST_USER_NICKNAME);
        } else {
            findMember.updateMemberInfo(dto);
        }

    }

    // 닉네임 중복확인
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EXIST_USER_NICKNAME);
        }
    }

}
