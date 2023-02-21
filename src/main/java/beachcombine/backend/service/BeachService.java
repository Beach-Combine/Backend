package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.dto.response.BeachLatestRecordResponse;
import beachcombine.backend.repository.BeachRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BeachService {

    private final BeachRepository beachRepository;
    private final RecordRepository recordRepository;
    private final ImageService imageService;

    // 해변 뱃지 조회
    @Transactional(readOnly = true)
    public BeachBadgeResponse findBadgeImage(Long beachId) {

        Beach findBeach = getBeachOrThrow(beachId);

        String imageUrl = imageService.processImage(findBeach.getBadgeImage());

        BeachBadgeResponse response = BeachBadgeResponse.builder()
                .id(findBeach.getId())
                .badgeImage(imageUrl)
                .build();

        return response;
    }

    // 해변 상세 조회 (최근 청소 기록 제공)
    @Transactional(readOnly = true)
    public BeachLatestRecordResponse findLatestRecord(Long beachId) {

        Beach findBeach = getBeachOrThrow(beachId);
        Record findRecord = recordRepository.findTopByBeachIdOrderByCreatedDateDesc(beachId);

        if(findRecord == null || !findRecord.getMember().getProfilePublic()) { // 청소 기록 없거나 멤버가 프로필 비공개 설정했을 때
            BeachLatestRecordResponse response = BeachLatestRecordResponse.builder()
                    .beach(BeachLatestRecordResponse.BeachDto.from(findBeach))
                    .build();

            return response; // 해변 이름만 보내고 나머지는 null로 보냄
        }

        String beforeImageUrl = imageService.processImage(findRecord.getBeforeImage());
        String afterImageUrl = imageService.processImage(findRecord.getAfterImage());

        BeachLatestRecordResponse response = BeachLatestRecordResponse.builder()
                .beach(BeachLatestRecordResponse.BeachDto.from(findBeach))
                .record(BeachLatestRecordResponse.RecordDto.from(findRecord, beforeImageUrl, afterImageUrl))
                .member(BeachLatestRecordResponse.MemberDto.from(findRecord.getMember()))
                .build();

        return response;
    }

    // 예외 처리 - 존재하는 beach 인가
    private Beach getBeachOrThrow(Long beachId) {

        return beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));
    }
}