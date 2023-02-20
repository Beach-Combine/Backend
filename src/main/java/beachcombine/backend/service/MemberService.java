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
    public MemberResponse getMember(Long memberId) {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);

        // 이미지 처리
        String imageUrl = imageService.processImage(findMember.getImage());

        MemberResponse response = findMember.getMember(imageUrl);
        return response;
    }

    // 회원 정보 수정
    public void updateMember(Long memberId, MemberUpdateRequest request) throws IOException {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);

        // 중복 검증
        if (findMember.isUpdatedNickname(request.getNickname())) {
            checkNicknameDuplicate(request.getNickname());
        }

        // 이미지 업로드
        String uuid = null; // 유저가 이미지를 없애도록 수정한 경우

        if (!request.getImage().isEmpty()) { // 유저가 이미지를 다른 파일로 수정한 경우
            uuid = imageService.uploadImage(request.getImage()); // GCS에 이미지 업로드한 후, UUID 값만 받아와 DB에 저장함
        }

        findMember.updateMember(request, uuid);
    }

    // 닉네임 중복확인
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {

        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EXIST_USER_NICKNAME);
        }
    }

    // 프로필 공개여부 지정
    public void updateProfilePublic(Long memberId, Boolean option) {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);

        findMember.updateProfilePublic(option);
    }

    // 포인트 받기
    public void updateMemberPoint(Long memberId, int option) {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);

        if (!findMember.updateMemberPoint(option)) {
            throw new CustomException(ErrorCode.BAD_REQUEST_OPTION_VALUE);
        }
    }

    // 예외 처리 - 존재하는 member인지
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
