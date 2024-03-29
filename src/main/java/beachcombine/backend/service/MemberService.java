package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.MemberPreferredFeed;
import beachcombine.backend.domain.Notification;
import beachcombine.backend.dto.response.MemberRankingResponse;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.dto.response.NotificationResponse;
import beachcombine.backend.dto.response.TrashcanMarkerResponse;
import beachcombine.backend.event.MemberEvent;
import beachcombine.backend.event.NotificationCode;
import beachcombine.backend.repository.FeedRepository;
import beachcombine.backend.repository.MemberPreferredFeedRepository;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final MemberPreferredFeedRepository memberPreferredFeedRepository;
    private final NotificationRepository notificationRepository;
    private final ImageService imageService;
    private final ApplicationEventPublisher eventPublisher;

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

        // 알림 생성
        if (option == 0) { // 기존 등록된 쓰레기통
            eventPublisher.publishEvent(new MemberEvent(findMember, NotificationCode.CLEANING_AND_TRASH_DISPOSAL));
        }
        if (option == 1) {
            eventPublisher.publishEvent(new MemberEvent(findMember, NotificationCode.CLEANING_WITHOUT_TRASH_DISPOSAL));
        }
    }

    // '월별 포인트' 초기화
    @Scheduled(cron="0 0 0 1 * ?", zone = "Asia/Seoul") // 초 분 시 일 월 요일
    public void resetMonthPoint() {

        memberRepository.findAll().forEach(m -> m.resetMonthPoint());
    }

   // 랭킹 조회
    public MemberRankingResponse getMemberRanking(String range, int pageSize, Long lastId, Integer lastPoint) {

        List<Member> memberList = new ArrayList<>();

        if (range.equals("all")) {
            memberList = memberRepository.findByTotalPointRanking(pageSize, lastId, lastPoint);
        }
        if (range.equals("month")) {
            memberList = memberRepository.findByMonthPointRanking(pageSize, lastId, lastPoint);
        }
        if (memberList.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST_OPTION_VALUE);
        }

        boolean nextPage = false;
        if (memberList.size() == pageSize + 1) {
            nextPage = true;
            memberList.remove(pageSize + 0); // pageSize+1개만큼 가져옴. 마지막 원소 삭제 필요
        }

        return MemberRankingResponse.builder()
                .nextPage(nextPage)
                .memberDtoList(memberList.stream()
                        .map(m -> MemberRankingResponse.MemberDto.of(m, imageService.processImage(m.getImage()), range))
                        .collect(Collectors.toList()))
                .build();
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

    // 알람 목록 조회
    public List<NotificationResponse> getNotificationList(Long memberId) {

        List<Notification> findNotificationList  = notificationRepository.findAllByMemberId(memberId);
        List<NotificationResponse> responseList = findNotificationList.stream()
                .map(m -> NotificationResponse.builder()
                        .notificationId(m.getId())
                        .memberId(m.getMember().getId())
                        .title(m.getTitle())
                        .message(m.getMessage())
                        .details(m.getDetails())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }

    // 튜토리얼 완료 등록
    public Long completeTutorial(Long memberId) {

        Member findMember = getMemberOrThrow(memberId);

        findMember.updateTutorialCompleted();

        return findMember.getId();
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
