package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse findMemberInfo(long id) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return findMember.getMemberInfo();
    }

    // 회원 정보 수정
    public void updateMemberInfo(Long id, MemberUpdateRequest dto) throws IOException {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 중복 검증
        if (findMember.isUpdatedNickname(dto.getNickname())) {
            checkNicknameDuplicate(dto.getNickname());
        }

        // 이미지 업로드
        String uuid = null; // 유저가 이미지를 없애도록 수정한 경우

        if(!dto.getImage().isEmpty()) { // 유저가 이미지를 다른 파일로 수정한 경우
            uuid = imageService.uploadImage(dto.getImage()); // GCS에 이미지 업로드한 후, UUID 값만 받아와 DB에 저장함
        }

        // DB 업데이트
        findMember.updateMemberInfo(dto, uuid);
    }

    // 닉네임 중복확인
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {

        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EXIST_USER_NICKNAME);
        }
    }

    // 프로필 공개여부 지정
    public void UpdateProfilePublic(Long id, Boolean option) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        findMember.updateProfilePublic(option);
    }

    // 포인트 받기
    public void UpdateMemberPoint(Long id, int option) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        if (!findMember.updateMemberPoint(option)){
            throw new CustomException(ErrorCode.BAD_REQUEST_OPTION_VALUE);
        }
    }
}
