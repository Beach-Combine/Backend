package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.FeedSaveRequest;
import beachcombine.backend.dto.response.FeedResponse;
import beachcombine.backend.dto.response.TrashcanMarkerResponse;
import beachcombine.backend.repository.FeedRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class FeedService {

    private final FeedRepository feedRepository;
    private final RecordRepository recordRepository;
    private final ImageService imageService;

    // 피드 기록하기
    public Long saveFeed(Long memberId, FeedSaveRequest request, Long recordId) throws IOException {

        Record findRecord = getRecordOrThrow(recordId);
        if(findRecord.getFeed() != null ){
            throw new CustomException(ErrorCode.EXIST_FEED_RECORD);
        }


        Feed feed = Feed.builder()
                .review(request.getReview())
                .build();
        findRecord.setFeed(feed);
        feedRepository.save(feed);

        return feed.getId();
    }

    // 예외 처리 - 존재하는 record 인가
    private Record getRecordOrThrow(Long id) {

        return recordRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECORD));
    }

    // 피드 목록 조회
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeedList() {

        List<Feed> findFeedList = feedRepository.findAllByOrderByCreatedDateDesc();
        List<FeedResponse> responseList = new ArrayList<>();

        for(Feed feed: findFeedList){
            Record record = feed.getRecord();
            Member member = record.getMember();
            String beforeImageUrl = imageService.processImage(record.getBeforeImage());
            String afterImageUrl = imageService.processImage(record.getAfterImage());

            FeedResponse feedResponse = FeedResponse.builder()
                    .id(feed.getId())
                    .review(feed.getReview())
                    .recordId(record.getId())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .beforeImage(beforeImageUrl)
                    .afterImage(afterImageUrl)
                    .beachName(record.getBeach().getName())
                    .build();
            responseList.add(feedResponse);
        }

        return responseList;
    }
}
