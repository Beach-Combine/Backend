package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.response.BeachLatestRecordResponse;
import beachcombine.backend.dto.response.BeachMarkerResponse;
import beachcombine.backend.repository.BeachRepository;
import beachcombine.backend.repository.RecordRepository;
import beachcombine.backend.util.RayCastingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final RayCastingUtil rayCastingUtil;


    // 해변 상세 조회 (최근 청소 기록 제공)
    @Transactional(readOnly = true)
    public BeachLatestRecordResponse findLatestRecord(Long beachId) {

        Beach findBeach = getBeachOrThrow(beachId);
        Record findRecord = getLatestRecord(beachId);

        if (findRecord == null || !findRecord.getMember().getProfilePublic()) { // 청소 기록 없거나 멤버가 프로필 비공개 설정했을 때
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

    // (지도) 전체 해변 위치 조회
    @Transactional(readOnly = true)
    public List<BeachMarkerResponse> findBeachMarkers() {

        List<Beach> findBeachList = beachRepository.findAll();
        List<BeachMarkerResponse> responseList = findBeachList.stream()
                .map(m -> BeachMarkerResponse.builder()
                        .id(m.getId())
                        .lat(String.valueOf(m.getLat()))
                        .lng(String.valueOf(m.getLng()))
                        .memberImage(getRecordMemberImage(m))
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }

    public String getRecordMemberImage(Beach beach) {

        Record findRecord = getLatestRecord(beach.getId());
        if (findRecord == null) {
            return "none";
        }

        Member findMember = findRecord.getMember();
        if (!findMember.getProfilePublic()) { // 멤버가 프로필 비공개 설정했을 때
            return "lock";
        }

        return imageService.processImage(findMember.getImage());
    }

    public Record getLatestRecord(Long beachId) {

        return recordRepository.findTopByBeachIdOrderByCreatedDateDesc(beachId);
    }

    // 예외 처리 - 존재하는 beach 인가
    private Beach getBeachOrThrow(Long beachId) {

        return beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));
    }

    // 해변 근처 인증하기
    @Transactional(readOnly = true)
    public void verifyNearBeach(Long beachId, BigDecimal lat, BigDecimal lng) {

        Beach findBeach = getBeachOrThrow(beachId);
        String beachRange = findBeach.getBeachRange();
        beachRange = beachRange.replace("{", "");
        beachRange = beachRange.replace(" ", "");
        beachRange = beachRange.substring(0, beachRange.length() - 1);
        String[] s1 = beachRange.split("},");

        List<BigDecimal> xCoords = new ArrayList();
        List<BigDecimal> yCoords = new ArrayList();
        for (int i = 0; i < s1.length; i++) {
            String[] group1 = s1[i].split(",");
            xCoords.add(new BigDecimal(group1[0]));
            yCoords.add(new BigDecimal(group1[1]));
        }

        boolean isInside = rayCastingUtil.isInsidePolygon(xCoords, yCoords, lat, lng);

        if (!isInside) {
            throw new CustomException(ErrorCode.NOT_NEAR_BEACH);
        }
    }
}
