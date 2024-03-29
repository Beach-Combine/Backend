package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.dto.response.*;
import beachcombine.backend.repository.BeachRepository;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final BeachRepository beachRepository;

    // 청소 기록하기
    public Long saveRecord(Long memberId, RecordSaveRequest request, Long beachId) throws IOException {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);
        Beach findBeach = getBeachOrThrow(beachId);
        checkExistsImage(request.getAfterImage());
        checkExistsImage(request.getBeforeImage());

        // 이미지 업로드
        String beforeUuid = imageService.uploadImage(request.getBeforeImage());
        String afterUuid = imageService.uploadImage(request.getAfterImage());

        Record record = Record.builder()
                .duration(request.getTime())
                .distance(request.getRange())
                .beforeImage(beforeUuid)
                .afterImage(afterUuid)
                .build();

        record.setMember(findMember);
        record.setBeach(findBeach);
        recordRepository.save(record);

        return record.getId();
    }

    // 청소기록 목록 조회
    public List<RecordResponse> getRecordList(Long memberId) {

        List<RecordResponse> responseList = new ArrayList<>();
        List<Record> recordList = recordRepository.findAllByMemberId(memberId);
        for (Record record: recordList){
            Boolean isWritten = (record.getFeed() != null);
            String beforeImageUrl = imageService.processImage(record.getBeforeImage());
            String afterImageUrl = imageService.processImage(record.getAfterImage());
            RecordResponse recordResponse = RecordResponse.builder()
                    .recordId(record.getId())
                    .time(record.getDuration())
                    .date(record.getCreatedDate())
                    .range(record.getDistance())
                    .beforeImage(beforeImageUrl)
                    .afterImage(afterImageUrl)
                    .isWritten(isWritten)
                    .beachId(record.getBeach().getId())
                    .beachName(record.getBeach().getName())
                    .build();
            responseList.add(recordResponse);
        }

        return responseList;
    }

    // 마이페이지 - (지도) 청소한 해변 조회
    public List<MyBeachMarkerResponse> getMyBeachMarker(Long memberId) {

        Member findMember = getMemberOrThrow(memberId);
        List<Beach> beachList = recordRepository.findBeachList(memberId);
        List<MyBeachMarkerResponse> responseList = new ArrayList<>();
        for (Beach beach: beachList){
            String imageUrl = imageService.processImage(beach.getBadgeImage());
            MyBeachMarkerResponse myBeachMarkerResponse = MyBeachMarkerResponse.builder()
                    .id(beach.getId())
                    .name(beach.getName())
                    .lat(beach.getLat().toString())
                    .lng(beach.getLng().toString())
                    .build();
            responseList.add(myBeachMarkerResponse);
        }
        return responseList;
    }

    // 마이페이지 - (지도) 특정 위치 청소 기록 목록 조회
    public BeachRecordListResponse getMyBeachRecord(Long memberId, Long beachId) {

        Member findMember = getMemberOrThrow(memberId);
        Beach findBeach = getBeachOrThrow(beachId);
        List<Record> recordList = recordRepository.findMyBeachRecord(memberId, beachId);

        BeachRecordListResponse beachRecordListResponse = BeachRecordListResponse.builder()
                .beach(BeachRecordListResponse.BeachDto.from(findBeach))
                .recordList(recordList.stream()
                        .map(r -> BeachRecordListResponse.RecordDto.from(r, imageService.processImage(r.getBeforeImage()), imageService.processImage(r.getAfterImage())))
                        .collect(Collectors.toList()))
                .build();
        return beachRecordListResponse;
    }


    // 예외 처리 - 존재하는 member 인가
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 존재하는 beach 인가
    private Beach getBeachOrThrow(Long id) {

        return beachRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));
    }

    // 예외 처리 - 이미지 있는지
    private void checkExistsImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }
}
