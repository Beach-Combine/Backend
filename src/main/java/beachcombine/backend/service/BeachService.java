package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.dto.response.BeachMarkerResponse;
import beachcombine.backend.repository.BeachRepository;
import beachcombine.backend.repository.RecordRepository;
import beachcombine.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BeachService {

    private final BeachRepository beachRepository;
    private final RecordRepository recordRepository;
    private final ImageService imageService;

    private final String defaultBeachImage = "defaultImageLink";
    private final String hiddenProfileImage = "hiddenImageLink";

    // 해변 뱃지 조회
    @Transactional(readOnly = true)
    public BeachBadgeResponse findBadgeImage(Long beachId) {

        Beach findBeach = beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));

        return findBeach.getBeachBadgeImage();
    }

    // (지도) 전체 해변 위치 조회
    @Transactional(readOnly = true)
    public List<BeachMarkerResponse> findBeachMarkers() {


        List<Beach> findBeaches = beachRepository.findAll();
        List<BeachMarkerResponse> beachResponseList = findBeaches.stream()
                .map(m -> BeachMarkerResponse.builder()
                    .id(m.getId())
                    .lat(String.valueOf(m.getLat()))
                    .lng(String.valueOf(m.getLng()))
                    .image(getRecordMemberImage(m))
                    .build())
                .collect(Collectors.toList());
        return beachResponseList;
    }

    public String getRecordMemberImage(Beach beach) {

        Record findRecord = getLatestRecord(beach.getId());
        if (findRecord == null) {
            return imageService.processImage("defaultImageLink");
        }
        Member findMember = findRecord.getMember();
        if (!findMember.getProfilePublic()) { // 멤버가 프로필 비공개 설정했을 때
            return imageService.processImage("hiddenImageLink");
        }
        return imageService.processImage(findMember.getImage());
    }

    public Record getLatestRecord(Long beachId) {

        Record findRecord = recordRepository.findTopByBeachIdOrderByCreatedDateDesc(beachId);
        return findRecord;
    }
}
