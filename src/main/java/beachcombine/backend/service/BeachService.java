package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.dto.response.BeachMarkerImageResponse;
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

    private final String defaultBeachImage= "defaultImageLink";
    private final String hiddenProfileImage= "hiddenImageLink";

    // 해변 뱃지 조회
    @Transactional(readOnly = true)
    public BeachBadgeResponse findBadgeImage(Long beachId) {

        Beach findBeach = beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));

        return findBeach.getBeachBadgeImage();
    }

    // (지도) 최근 청소 기록 조회
    @Transactional(readOnly = true)
    public BeachMarkerImageResponse findLatestRecord(Long beachId) {

        Beach findBeach = beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));
        if(findBeach.getRecordId()==null) { // 청소기록 없을 때
            return BeachMarkerImageResponse.builder()
                    .latestRecordImage(defaultBeachImage)
                    .build();
        }
        Record findRecord = recordRepository.findById(findBeach.getRecordId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECORD));
        Member findMember = findRecord.getMember();
        if(!findMember.getProfilePublic()) { // 멤버가 프로필 비공개 설정했을 때
            return BeachMarkerImageResponse.builder()
                    .latestRecordImage(hiddenProfileImage)
                    .build();
        }
        return findMember.getProfileImage();
    }

}
