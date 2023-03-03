package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.FeedSaveRequest;
import beachcombine.backend.repository.FeedRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {

    private final FeedRepository feedRepository;
    private final RecordRepository recordRepository;

    // 피드 기록하기
    public Long saveFeed(Long memberId, FeedSaveRequest request, Long recordId) throws IOException {

        Record findRecord = getRecordOrThrow(recordId);

        Feed feed = Feed.builder()
                .review(request.getReview())
                .build();

        feed.setRecord(findRecord);
        feedRepository.save(feed);

        return feed.getId();
    }

    // 예외 처리 - 존재하는 record 인가
    private Record getRecordOrThrow(Long id) {

        return recordRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECORD));
    }
}
