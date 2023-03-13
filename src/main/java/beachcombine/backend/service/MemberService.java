package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.MemberPreferredFeed;
import beachcombine.backend.dto.response.MemberRankingResponse;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.repository.FeedRepository;
import beachcombine.backend.repository.MemberPreferredFeedRepository;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final MemberPreferredFeedRepository memberPreferredFeedRepository;
    private final ImageService imageService;

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse getMember(Long memberId) {

        Member findMember = getMemberOrThrow(memberId);

        String imageUrl = imageService.processImage(findMember.getImage());

        MemberResponse response = MemberResponse.builder()
                .id(findMember.getId())
                .email(findMember.getEmail())
                .nickname(findMember.getNickname())
                .image(imageUrl)
                .totalPoint(findMember.getTotalPoint())
                .monthPoint(findMember.getMonthPoint())
                .purchasePoint(findMember.getPurchasePoint())
                .profilePublic(findMember.getProfilePublic())
                .role(findMember.getRole())
                .build();

        return response;
    }

    // 회원 정보 수정
    public void updateMember(Long memberId, MemberUpdateRequest request) throws IOException {

        Member findMember = getMemberOrThrow(memberId);

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
            throw new CustomException(ErrorCode.EXIST_MEMBER_NICKNAME);
        }
    }

    // 프로필 공개여부 지정
    public void updateProfilePublic(Long memberId, Boolean option) {

        Member findMember = getMemberOrThrow(memberId);

        findMember.updateProfilePublic(option);
    }

    // 포인트 받기
    public void updateMemberPoint(Long memberId, int option) {

        Member findMember = getMemberOrThrow(memberId);

        if (!findMember.updateMemberPoint(option)) {
            throw new CustomException(ErrorCode.BAD_REQUEST_OPTION_VALUE);
        }
    }

    // 랭킹 조회
    public List<MemberRankingResponse> getMemberRanking(String range, int pageSize, Long lastId, Integer lastPoint) {

        List<Member> memberList = new ArrayList<>();
        List<MemberRankingResponse> response = new ArrayList<>();

        if (range.equals("all")) {
            memberList = memberRepository.findByTotalPointRanking(pageSize, lastId, lastPoint);
        }
        if (range.equals("month")) {
            memberList = memberRepository.findByMonthPointRanking(pageSize, lastId, lastPoint);
        }
        if (memberList.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST_OPTION_VALUE);
        }

        for (Member member : memberList) {
            String imageUrl = imageService.processImage(member.getImage());
            response.add(MemberRankingResponse.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .image(imageUrl)
                    .point(range.equals("all") ? member.getTotalPoint() : member.getMonthPoint())
                    .build());
        }

        return response;
    }

    // 회원-피드 좋아요 관계 등록 (피드 좋아요하기)
    public Long likeFeed(Long memberId, Long feedId) {

        Member findMember = getMemberOrThrow(memberId);
        Feed findFeed = getFeedOrThrow(feedId);

        // 중복 예외 처리
        if(memberPreferredFeedRepository.existsByMemberAndFeed(findMember, findFeed)) {
            throw new CustomException(ErrorCode.EXIST_MEMBER_PREFERRED_FEED);
        }

        MemberPreferredFeed memberPreferredFeed = MemberPreferredFeed.builder()
                .member(findMember)
                .feed(findFeed)
                .build();

        memberPreferredFeedRepository.save(memberPreferredFeed);

        return memberPreferredFeed.getId();
    }

    // 회원-피드 좋아요 관계 취소 (피드 좋아요 취소하기)
    public void deleteLikeFeed(Long memberId, Long feedId) {

        Member findMember = getMemberOrThrow(memberId);
        Feed findFeed = getFeedOrThrow(feedId);

        memberPreferredFeedRepository.deleteByMemberAndFeed(findMember, findFeed);
    }

    // 회원 잔여 포인트 조회
    public Integer getMemberPoint(Long memberId) {

        Member findMember = getMemberOrThrow(memberId);

        return findMember.getTotalPoint() - findMember.getPurchasePoint();
    }

    // 예외 처리 - 존재하는 member인지
    private Member getMemberOrThrow(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 존재하는 feed인지
    private Feed getFeedOrThrow(Long feedId) {

        return feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FEED));
    }
}
