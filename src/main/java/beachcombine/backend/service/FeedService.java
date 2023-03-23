package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.FeedSaveRequest;
import beachcombine.backend.dto.response.FeedResponse;
import beachcombine.backend.repository.FeedRepository;
import beachcombine.backend.repository.MemberPreferredFeedRepository;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {

    private final FeedRepository feedRepository;
    private final RecordRepository recordRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final MemberPreferredFeedRepository memberPreferredFeedRepository;

    // 피드 기록하기
    public Long saveFeed(Long memberId, FeedSaveRequest request, Long recordId) {

        Member findMember = getMemberOrThrow(memberId);
        Record findRecord = getRecordOrThrow(recordId);

        if (findRecord.getFeed() != null) {
            throw new CustomException(ErrorCode.EXIST_FEED_RECORD);
        }

        checkPermissionByRecord(findMember, findRecord);

        Feed feed = Feed.builder()
                .review(request.getReview())
                .build();

        findRecord.setFeed(feed); // feed.setRecord(findRecord);
        feedRepository.save(feed);

        return feed.getId();
    }

    // 피드 삭제하기
    public void deleteFeed(Long memberId, Long feedId) {

        Member findMember = getMemberOrThrow(memberId);
        Feed findFeed = getFeedOrThrow(feedId);

        checkPermissionByFeed(findMember, findFeed);

        Record findRecord = findFeed.getRecord(); // 연관된 Record 가져오기
        if(findRecord != null) {
            findRecord.setFeed(null);
        }

        feedRepository.deleteById(feedId); // Feed 삭제
    }

    // 피드 목록 조회
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeedList(Long memberId) {

        Member findMember = getMemberOrThrow(memberId);

        List<Feed> findFeedList = feedRepository.findAllByOrderByCreatedDateDesc();
        List<FeedResponse> responseList = new ArrayList<>();

        for (Feed feed : findFeedList) {
            Record record = feed.getRecord();
            Member member = record.getMember();
            String beforeImageUrl = imageService.processImage(record.getBeforeImage());
            String afterImageUrl = imageService.processImage(record.getAfterImage());
            String memberImage = imageService.processImage(member.getImage());

            FeedResponse feedResponse = FeedResponse.builder()
                    .id(feed.getId())
                    .review(feed.getReview())
                    .recordId(record.getId())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .beforeImage(beforeImageUrl)
                    .afterImage(afterImageUrl)
                    .beachName(record.getBeach().getName())
                    .isPreferred(memberPreferredFeedRepository.existsByMemberAndFeed(findMember, feed))
                    .like(memberPreferredFeedRepository.countByFeed(feed))
                    .memberImage(memberImage)
                    .build();
            responseList.add(feedResponse);
        }

        return responseList;
    }

    // 예외 처리 - 존재하는 record 인가
    private Record getRecordOrThrow(Long id) {

        return recordRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECORD));
    }

    // 예외 처리 - 존재하는 feed 인가
    private Feed getFeedOrThrow(Long id) {

        return feedRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FEED));
    }

    // 예외 처리 - 존재하는 member 인가
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 삭제/편집 권한을 가진 member 인가
    private void checkPermissionByFeed(Member member, Feed feed) {

        if (!feed.getRecord().getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }

    // 예외 처리 - 삭제/편집 권한을 가진 member 인가
    private void checkPermissionByRecord(Member member, Record record) {

        if (!record.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }

}
